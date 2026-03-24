const state = {
    products: [],
    filteredProducts: [],
    cart: null,
    token: localStorage.getItem("accessToken") || "",
    user: null,
};

const elements = {
    authFeedback: document.querySelector("#authFeedback"),
    authStateLabel: document.querySelector("#authStateLabel"),
    cartCount: document.querySelector("#cartCount"),
    cartEmptyState: document.querySelector("#cartEmptyState"),
    cartFeedback: document.querySelector("#cartFeedback"),
    cartIdLabel: document.querySelector("#cartIdLabel"),
    cartItems: document.querySelector("#cartItems"),
    cartTotal: document.querySelector("#cartTotal"),
    catalogEmptyState: document.querySelector("#catalogEmptyState"),
    createCartButton: document.querySelector("#createCartButton"),
    loginForm: document.querySelector("#loginForm"),
    logoutButton: document.querySelector("#logoutButton"),
    productCount: document.querySelector("#productCount"),
    productGrid: document.querySelector("#productGrid"),
    refreshProductsButton: document.querySelector("#refreshProductsButton"),
    registerForm: document.querySelector("#registerForm"),
    searchInput: document.querySelector("#searchInput"),
    sessionMessage: document.querySelector("#sessionMessage"),
    userDetails: document.querySelector("#userDetails"),
};

document.querySelectorAll("[data-tab-target]").forEach((button) => {
    button.addEventListener("click", () => {
        document.querySelectorAll(".tab-button").forEach((item) => item.classList.remove("active"));
        document.querySelectorAll(".tab-panel").forEach((panel) => panel.classList.remove("active"));
        button.classList.add("active");
        document.getElementById(button.dataset.tabTarget).classList.add("active");
        clearMessage(elements.authFeedback);
    });
});

elements.loginForm.addEventListener("submit", handleLogin);
elements.registerForm.addEventListener("submit", handleRegister);
elements.refreshProductsButton.addEventListener("click", () => loadProducts(true));
elements.createCartButton.addEventListener("click", () => ensureCart(true));
elements.searchInput.addEventListener("input", handleSearch);
elements.logoutButton.addEventListener("click", logout);

bootstrap();

async function bootstrap() {
    renderAuthState();
    await Promise.all([loadProducts(false), ensureCart(false)]);
    if (state.token) {
        await loadCurrentUser();
    }
}

async function handleLogin(event) {
    event.preventDefault();
    const formData = new FormData(event.currentTarget);
    setPending(event.currentTarget, true);
    try {
        const response = await fetchJson("/auth/login", {
            method: "POST",
            body: {
                email: String(formData.get("email") || "").trim(),
                password: String(formData.get("password") || ""),
            },
        });

        state.token = response.token;
        localStorage.setItem("accessToken", state.token);
        showMessage(elements.authFeedback, "Signed in successfully.");
        event.currentTarget.reset();
        await loadCurrentUser();
    } catch (error) {
        showMessage(elements.authFeedback, error.message || "Unable to sign in right now.", true);
    } finally {
        setPending(event.currentTarget, false);
    }
}

async function handleRegister(event) {
    event.preventDefault();
    const formData = new FormData(event.currentTarget);
    const payload = {
        name: String(formData.get("name") || "").trim(),
        email: String(formData.get("email") || "").trim().toLowerCase(),
        password: String(formData.get("password") || ""),
    };

    setPending(event.currentTarget, true);
    try {
        await fetchJson("/users", { method: "POST", body: payload });
        showMessage(elements.authFeedback, "Account created. Signing you in...");
        event.currentTarget.reset();
        await loginAfterRegister(payload.email, payload.password);
    } catch (error) {
        showMessage(elements.authFeedback, error.message || "Unable to create account.", true);
    } finally {
        setPending(event.currentTarget, false);
    }
}

async function loginAfterRegister(email, password) {
    const response = await fetchJson("/auth/login", {
        method: "POST",
        body: { email, password },
    });

    state.token = response.token;
    localStorage.setItem("accessToken", state.token);
    await loadCurrentUser();
}

async function loadCurrentUser() {
    if (!state.token) {
        state.user = null;
        renderAuthState();
        return;
    }

    try {
        state.user = await fetchJson("/auth/me", {
            headers: { Authorization: `Bearer ${state.token}` },
        });
    } catch (error) {
        state.user = null;
        state.token = "";
        localStorage.removeItem("accessToken");
        showMessage(elements.authFeedback, "Session expired. Please sign in again.", true);
    }

    renderAuthState();
}

async function loadProducts(showToast) {
    try {
        state.products = await fetchJson("/products");
        state.filteredProducts = [...state.products];
        renderProducts();
        elements.productCount.textContent = String(state.products.length);
        if (showToast) {
            showMessage(elements.cartFeedback, "Catalog refreshed.");
        }
    } catch (error) {
        elements.productGrid.innerHTML = "";
        elements.catalogEmptyState.classList.remove("hidden");
        showMessage(elements.cartFeedback, error.message || "Unable to load products.", true);
    }
}

function handleSearch(event) {
    const query = event.target.value.trim().toLowerCase();
    state.filteredProducts = state.products.filter((product) => {
        const haystack = `${product.name} ${product.description || ""}`.toLowerCase();
        return haystack.includes(query);
    });
    renderProducts();
}

function renderProducts() {
    const products = state.filteredProducts;
    elements.catalogEmptyState.classList.toggle("hidden", products.length > 0);
    elements.productGrid.innerHTML = products.map((product) => `
        <article class="product-card">
            <span class="product-category">Category ${product.categoryId ?? "NA"}</span>
            <h3>${escapeHtml(product.name)}</h3>
            <p class="product-meta">${escapeHtml(product.description || "No description added yet.")}</p>
            <div class="price-row">
                <strong class="price">${formatCurrency(product.price)}</strong>
                <span>ID ${product.id}</span>
            </div>
            <button class="add-button" type="button" data-product-id="${product.id}">
                Add to cart
            </button>
        </article>
    `).join("");

    elements.productGrid.querySelectorAll("[data-product-id]").forEach((button) => {
        button.addEventListener("click", () => addToCart(Number(button.dataset.productId)));
    });
}

async function ensureCart(forceNewCart) {
    const storedCartId = localStorage.getItem("cartId");

    if (!forceNewCart && storedCartId) {
        try {
            state.cart = await fetchJson(`/carts/${storedCartId}`);
            renderCart();
            return;
        } catch (error) {
            localStorage.removeItem("cartId");
        }
    }

    try {
        state.cart = await fetchJson("/carts", { method: "POST" });
        localStorage.setItem("cartId", state.cart.id);
        renderCart();
        showMessage(elements.cartFeedback, forceNewCart ? "Fresh cart created." : "Cart is ready.");
    } catch (error) {
        showMessage(elements.cartFeedback, error.message || "Unable to create cart.", true);
    }
}

async function addToCart(productId) {
    if (!state.cart?.id) {
        await ensureCart(false);
    }

    try {
        await fetchJson(`/carts/${state.cart.id}/items`, {
            method: "POST",
            body: { productId },
        });
        await refreshCart();
        showMessage(elements.cartFeedback, "Item added to cart.");
    } catch (error) {
        showMessage(elements.cartFeedback, error.message || "Unable to add item.", true);
    }
}

async function refreshCart() {
    if (!state.cart?.id) {
        return;
    }
    state.cart = await fetchJson(`/carts/${state.cart.id}`);
    renderCart();
}

async function updateQuantity(productId, nextQuantity) {
    if (!state.cart?.id) {
        return;
    }

    try {
        if (nextQuantity <= 0) {
            await fetch(`/carts/${state.cart.id}/items/${productId}`, { method: "DELETE" });
        } else {
            await fetchJson(`/carts/${state.cart.id}/items/${productId}`, {
                method: "PUT",
                body: { quantity: nextQuantity },
            });
        }
        await refreshCart();
    } catch (error) {
        showMessage(elements.cartFeedback, error.message || "Unable to update cart.", true);
    }
}

function renderCart() {
    const items = state.cart?.items || [];
    elements.cartIdLabel.textContent = state.cart?.id || "Not created yet";
    elements.cartTotal.textContent = formatCurrency(state.cart?.totalPrice || 0);
    elements.cartCount.textContent = String(items.reduce((total, item) => total + item.quantity, 0));
    elements.cartEmptyState.classList.toggle("hidden", items.length > 0);

    if (items.length === 0) {
        elements.cartItems.innerHTML = "";
        return;
    }

    elements.cartItems.innerHTML = items.map((item) => `
        <article class="cart-item">
            <div class="cart-item-header">
                <div>
                    <h3>${escapeHtml(item.cartItemProductDTO.name)}</h3>
                    <p>${formatCurrency(item.cartItemProductDTO.price)} each</p>
                </div>
                <strong>${formatCurrency(item.totalPrice)}</strong>
            </div>
            <div class="cart-item-actions">
                <div class="quantity-control">
                    <button class="quantity-button" type="button" data-quantity-action="decrease" data-product-id="${item.cartItemProductDTO.id}">-</button>
                    <span>${item.quantity}</span>
                    <button class="quantity-button" type="button" data-quantity-action="increase" data-product-id="${item.cartItemProductDTO.id}">+</button>
                </div>
                <button class="danger-button" type="button" data-remove-id="${item.cartItemProductDTO.id}">Remove</button>
            </div>
        </article>
    `).join("");

    elements.cartItems.querySelectorAll("[data-quantity-action]").forEach((button) => {
        const productId = Number(button.dataset.productId);
        const item = items.find((cartItem) => cartItem.cartItemProductDTO.id === productId);
        button.addEventListener("click", () => {
            const delta = button.dataset.quantityAction === "increase" ? 1 : -1;
            updateQuantity(productId, item.quantity + delta);
        });
    });

    elements.cartItems.querySelectorAll("[data-remove-id]").forEach((button) => {
        button.addEventListener("click", () => updateQuantity(Number(button.dataset.removeId), 0));
    });
}

function renderAuthState() {
    const isAuthenticated = Boolean(state.user && state.token);
    elements.authStateLabel.textContent = isAuthenticated ? "Signed in" : "Guest";
    elements.sessionMessage.textContent = isAuthenticated ? `Hello, ${state.user.name}` : "Browsing as guest";
    elements.userDetails.textContent = isAuthenticated
        ? `${state.user.email} is connected to protected endpoints.`
        : "Create an account or sign in to view your profile details.";
    elements.logoutButton.classList.toggle("hidden", !isAuthenticated);
}

function logout() {
    state.token = "";
    state.user = null;
    localStorage.removeItem("accessToken");
    renderAuthState();
    showMessage(elements.authFeedback, "You have been logged out.");
}

async function fetchJson(url, options = {}) {
    const response = await fetch(url, {
        method: options.method || "GET",
        headers: {
            "Content-Type": "application/json",
            ...(options.headers || {}),
        },
        credentials: "include",
        body: options.body ? JSON.stringify(options.body) : undefined,
    });

    if (!response.ok) {
        let errorMessage = "Request failed.";
        try {
            const data = await response.json();
            if (typeof data === "string") {
                errorMessage = data;
            } else if (data.message) {
                errorMessage = data.message;
            } else if (data.email) {
                errorMessage = data.email;
            }
        } catch (error) {
            errorMessage = response.status === 401 ? "Unauthorized request." : errorMessage;
        }
        throw new Error(errorMessage);
    }

    if (response.status === 204) {
        return null;
    }

    return response.json();
}

function showMessage(element, message, isError = false) {
    element.textContent = message;
    element.style.color = isError ? "#b93821" : "";
}

function clearMessage(element) {
    element.textContent = "";
    element.style.color = "";
}

function setPending(form, isPending) {
    form.querySelectorAll("button").forEach((button) => {
        button.disabled = isPending;
    });
}

function formatCurrency(value) {
    const amount = Number(value || 0);
    return new Intl.NumberFormat("en-US", {
        style: "currency",
        currency: "USD",
    }).format(amount);
}

function escapeHtml(value) {
    return String(value)
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll("\"", "&quot;")
        .replaceAll("'", "&#39;");
}
