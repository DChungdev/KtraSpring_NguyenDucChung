package com.samsung.nguyenducchung.Controller;

import com.samsung.nguyenducchung.Model.Entity.Order;
import com.samsung.nguyenducchung.Model.Entity.OrderItem;
import com.samsung.nguyenducchung.Model.Entity.Product;
import com.samsung.nguyenducchung.Model.Entity.Users;
import com.samsung.nguyenducchung.Model.Repository.ProductRepository;
import com.samsung.nguyenducchung.Model.ViewModel.Cart;
import com.samsung.nguyenducchung.Model.ViewModel.CartItem;
import com.samsung.nguyenducchung.Service.CartService;
import com.samsung.nguyenducchung.Service.OrderService;
import com.samsung.nguyenducchung.Service.ProductService;
import com.samsung.nguyenducchung.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.security.core.Authentication;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {
    @Autowired
    ProductService productService;

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/")
    public String home(Model model,  WebRequest request) {
        // Lấy giỏ hàng từ session
        Cart cart = cartService.getCart(request);

        List<Product> products = productService.findAll();

        // Kiểm tra xem giỏ hàng có sản phẩm không
        boolean hasItemsInCart = !cart.getItems().isEmpty();

        // Thêm giỏ hàng và thông tin vào model
        model.addAttribute("cart", cart);
        model.addAttribute("hasItemsInCart", hasItemsInCart);

        model.addAttribute("products", products);
        return "home";
    }

    @GetMapping("/search")
    public String search(@RequestParam(defaultValue = "") String keyword, Model model)
    {
        List<Product> result = productService.searchProduct(keyword);
        model.addAttribute("products", result);
        return "home";
    }

    // Thêm sản phẩm vào giỏ hàng
    @GetMapping("/addToCart")
    public String addToCart(@RequestParam Long productId, @RequestParam(defaultValue = "1") int quantity, WebRequest request, Model model) {
        cartService.addToCart(request, productId, quantity);
        return "redirect:/cart";  // Điều hướng đến trang giỏ hàng
    }

    // Hiển thị giỏ hàng
    @GetMapping("/cart")
    public String viewCart(WebRequest request, Model model) {
        Cart cart = cartService.getCart(request);
        double total = cartService.getTotal(request);
        model.addAttribute("cart", cart);
        model.addAttribute("total", total);
        return "cart";  // Hiển thị trang giỏ hàng
    }

    // Xóa sản phẩm khỏi giỏ hàng
    @PostMapping("/removeFromCart")
    public String removeFromCart(@RequestParam("productId") Long productId, WebRequest request) {
        cartService.removeFromCart(request, productId);  // Xóa sản phẩm khỏi giỏ hàng
        return "redirect:/cart";  // Điều hướng lại đến trang giỏ hàng
    }

//    @GetMapping("/checkout")
//    public String checkout(WebRequest request, Model model) {
//        return "redirect:/cart";
//    }

    // Phương thức checkout
    @GetMapping("/checkout")
    public String checkout(WebRequest request, Model model) {
        // Lấy giỏ hàng từ request
        Cart cart = cartService.getCart(request);

        // Kiểm tra người dùng đã đăng nhập chưa
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Lấy thông tin người dùng đang đăng nhập
        String username = authentication.getName();
        Users user = userService.findByUsername(username);

        // Tạo đơn hàng mới
        Order order = new Order();
        order.setUser(user);
        order.setStatus("Đang xử lý");

        // Lưu đơn hàng vào cơ sở dữ liệu
        orderService.save(order);

        // Giả sử cart.getItems() trả về Map<Long, CartItem>
        Map<Long, CartItem> cartItemsMap = cart.getItems();

    // Chuyển đổi Map thành List
        List<CartItem> items = new ArrayList<>(cartItemsMap.values());

        for (CartItem item : items) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(item.getProduct());
            orderItem.setQuantity(item.getQuantity());

            // Lưu OrderItem vào DB
            orderService.saveOrderItem(orderItem);
        }

// Sau khi checkout, giỏ hàng sẽ được làm trống
        cartService.clearCart(request);

        // Thêm thông báo thành công
        model.addAttribute("message", "Đặt hàng thành công! Cảm ơn bạn đã mua hàng.");

        return "success"; // Chuyển hướng đến trang thông báo đặt hàng thành công
    }

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        Users user = new Users();
        model.addAttribute("user", user);

        return "Login";
    }

    @PostMapping("/logout")
    public String logout(Model model) {
        return "redirect:/";
    }

}
