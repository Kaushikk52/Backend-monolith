package com.ecommerce.demo.services;

import com.ecommerce.demo.models.Cart;
import com.ecommerce.demo.models.Product;
import com.ecommerce.demo.repositories.CartRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartRepo cartRepo;

    @Autowired
    InventoryService inventoryService;

    public void addCart(String cartId){
        Cart cart = new Cart();
        cart.setId(cartId);
        cartRepo.save(cart);
    }

    public void removeCart(String cartId){
        if(cartRepo.existsById(cartId)){
            Cart cart = cartRepo.findById(cartId).orElseThrow(()-> new RuntimeException(cartId+" : Cart not Found..."));
            cartRepo.delete(cart);
        }else {
            throw new IllegalArgumentException("Cart with ID " + cartId + " doesn't exist");
        }

    }

    public Cart addToCart(String cartId, Long productId) {


        Product product = inventoryService.getProductById(productId);
        Cart cart = cartRepo.findById(cartId).orElseThrow(() -> new RuntimeException(cartId + " : Cart not Found..."));

        if (product.getStock() > 0) {
            List<Long> productList = cart.getProducts();

            // Check if the product is already in the cart and update the quantity accordingly
            int initialQty = cart.getQty();
            if (initialQty >= product.getStock()) {
                throw new IllegalArgumentException("Requested quantity exceeds available stock. Only " + product.getStock() + " products available.");
            }

            // Update cart's price and quantity for the existing products in the cart
            double currentCartPrice = 0.0;
            for (Long itemId : productList) {
                Product productItem = inventoryService.getProductById(itemId);
                currentCartPrice += productItem.getPrice();
            }
            cart.setPrice(currentCartPrice);

            // Add the new product to the cart
            productList.add(product.getId());
            cart.setProducts(productList);
            cart.setQty(productList.size());
            cart.setPrice(cart.getPrice() + product.getPrice());

            // Decrement the stock of the product in the inventory
            product.setStock(product.getStock() - 1);
            inventoryService.updateProduct(product);

            return cartRepo.save(cart);
        } else {
            throw new IllegalArgumentException("Product out of stock");
        }
    }
    public Cart removeFromCart(String cartId, Product product) {
        Cart cart = cartRepo.findById(cartId).orElseThrow(() -> new RuntimeException(cartId + " : Cart not Found..."));
        List<Long> productList = cart.getProducts();

        // Check if the product is in the cart
        if (!productList.contains(product.getId())) {
            throw new IllegalArgumentException("Product does not exist in the cart");
        }

        // Remove the product from the cart
        productList.remove(product.getId());
        cart.setProducts(productList);

        // Update the stock in the inventory
        product.setStock(product.getStock() + 1);
        inventoryService.updateProduct(product);

        // Update cart quantity and price
        cart.setQty(productList.size());

        // Recalculate the total price of the cart
        double newTotalPrice = 0.0;
        for (Long itemId : productList) {
            Product productItem = inventoryService.getProductById(itemId);
            newTotalPrice += productItem.getPrice();
        }
        cart.setPrice(newTotalPrice);

        // Save the updated cart
        return cartRepo.save(cart);
    }
}
