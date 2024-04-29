package com.bookappstore.service.impl;

import com.bookappstore.dto.cart.ShoppingCartDto;
import com.bookappstore.exception.EntityNotFoundException;
import com.bookappstore.mapper.ShoppingCartMapper;
import com.bookappstore.repository.cart.ShoppingCartRepository;
import com.bookappstore.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartMapper shoppingCartMapper;
    private final ShoppingCartRepository shoppingCartRepository;

    @Override
    @Transactional
    public ShoppingCartDto getShoppingCart(Long id) {
        return shoppingCartMapper.toDto(shoppingCartRepository
                .findByUserId(id).orElseThrow(
                        () -> new EntityNotFoundException(
                                "Can't find user by id" + id
                        )
                ));
    }
}
