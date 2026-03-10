package com.spring.ecom.mapper;

import com.spring.ecom.dtos.OrderDTO;
import com.spring.ecom.dtos.OrderItemDTO;
import com.spring.ecom.dtos.OrderProductDTO;
import com.spring.ecom.entities.Order;
import com.spring.ecom.entities.OrderItem;
import com.spring.ecom.entities.Product;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-10T14:36:07+0530",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class OrderMapperImpl implements OrderMapper {

    @Override
    public OrderDTO toDto(Order order) {
        if ( order == null ) {
            return null;
        }

        OrderDTO orderDTO = new OrderDTO();

        orderDTO.setId( order.getId() );
        if ( order.getStatus() != null ) {
            orderDTO.setStatus( order.getStatus().name() );
        }
        orderDTO.setCreatedAt( order.getCreatedAt() );
        orderDTO.setItems( orderItemSetToOrderItemDTOList( order.getItems() ) );
        orderDTO.setTotalPrice( order.getTotalPrice() );

        return orderDTO;
    }

    protected OrderProductDTO productToOrderProductDTO(Product product) {
        if ( product == null ) {
            return null;
        }

        OrderProductDTO orderProductDTO = new OrderProductDTO();

        orderProductDTO.setId( product.getId() );
        orderProductDTO.setName( product.getName() );
        orderProductDTO.setPrice( product.getPrice() );

        return orderProductDTO;
    }

    protected OrderItemDTO orderItemToOrderItemDTO(OrderItem orderItem) {
        if ( orderItem == null ) {
            return null;
        }

        OrderItemDTO orderItemDTO = new OrderItemDTO();

        orderItemDTO.setProduct( productToOrderProductDTO( orderItem.getProduct() ) );
        if ( orderItem.getQuantity() != null ) {
            orderItemDTO.setQuantity( orderItem.getQuantity() );
        }
        orderItemDTO.setTotalPrice( orderItem.getTotalPrice() );

        return orderItemDTO;
    }

    protected List<OrderItemDTO> orderItemSetToOrderItemDTOList(Set<OrderItem> set) {
        if ( set == null ) {
            return null;
        }

        List<OrderItemDTO> list = new ArrayList<OrderItemDTO>( set.size() );
        for ( OrderItem orderItem : set ) {
            list.add( orderItemToOrderItemDTO( orderItem ) );
        }

        return list;
    }
}
