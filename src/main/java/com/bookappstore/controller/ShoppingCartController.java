package com.bookappstore.controller;

import com.bookappstore.dto.cart.CartItemRequestDto;
import com.bookappstore.dto.cart.CartItemUpdateDto;
import com.bookappstore.dto.cart.ShoppingCartResponseDto;
import com.bookappstore.model.User;
import com.bookappstore.service.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.awt.print.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
@Tag(name = "Shopping Cart", description = "Endpoints for managing shopping cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Retrieve user's shopping cart",
            description = "Retrieves the user's shopping cart")
    public ShoppingCartResponseDto getShoppingCart(
            Authentication authentication,
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "id", direction = Sort.Direction.ASC)})
                    Pageable pageable) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.getShoppingCart(user.getId());
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Add book to the shopping cart",
            description = "Adds a book to the user's shopping cart")
    public ShoppingCartResponseDto addCartItem(
            Authentication authentication,
            @RequestBody @Valid CartItemRequestDto cartItemRequestDto) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.addCartItem(user.getId(), cartItemRequestDto);
    }

    @PutMapping("/cart-items/{cartItemId}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Update quantity of a book in the shopping cart",
            description = "Updates the quantity of a book in the user's shopping cart")
    public ShoppingCartResponseDto updateCartItem(
            Authentication authentication,
            @PathVariable @Positive Long cartItemId,
            @RequestBody @Valid CartItemUpdateDto cartItemRequestDto) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.updateCartItem(
                user.getId(),
                cartItemId,
                cartItemRequestDto);
    }

    @DeleteMapping("/cart-items/{cartItemId}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Remove a book from the shopping cart",
            description = "Removes a book from the user's shopping cart")
    public ShoppingCartResponseDto deleteCartItem(
            Authentication authentication,
            @PathVariable @Positive Long cartItemId) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.deleteCartItem(user.getId(), cartItemId);
    }
}
