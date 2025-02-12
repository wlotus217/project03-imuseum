package com.spring.Imuseum.view.order;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.spring.Imuseum.member.MemberVO;
import com.spring.Imuseum.order.CartService;
import com.spring.Imuseum.order.CartVO;
import com.spring.Imuseum.order.OrderPagePVO;
import com.spring.Imuseum.order.OrderPageVO;
import com.spring.Imuseum.order.OrderService;
import com.spring.Imuseum.order.OrderVO;
import com.spring.Imuseum.order.WishService;

@Controller

@SessionAttributes("member") 
public class OrderController { 
	@Autowired
	private OrderService orderService;
	@Autowired
	private WishService wishService;
	@Autowired
	private CartService cartService;
	
	@ModelAttribute("wishCnt")
	public int getWishCnt(Model model,MemberVO mvo,HttpSession session) {
		mvo = (MemberVO) session.getAttribute("member");
		if(mvo == null) {
			return 0;
		} else {
			int wishCnt = wishService.getWishCnt(mvo.getId());
			System.out.println("위시리스트 건수: " + wishCnt);
			model.addAttribute("wishCnt", wishCnt);
			return wishCnt;
		}
	}
	
	@ModelAttribute("cartCnt")
	public int getCartCnt(MemberVO mvo,HttpSession session) {
		mvo = (MemberVO) session.getAttribute("member");
		System.out.println(">> getCartCnt() 실행 : 장바구니 건수");
		if(mvo == null) {
			return 0;
		} else {
			CartVO vo = new CartVO();
			System.out.println(mvo.getId());
			vo.setId(mvo.getId());
			int cartCnt = cartService.getCartCnt(vo);
			System.out.println("장바구니 건수: " + cartCnt);
			return cartCnt;
		}
	}
	
	
	@RequestMapping("/getOrderPage.do")
	public String getOrderPage(OrderPageVO vo, Model model, @ModelAttribute("member") MemberVO mvo) {
		List<OrderPagePVO> list = orderService.getPInfo(vo.getOrders());
		System.out.println("orderList: " + list);
		System.out.println("list.size: " + list.size());
		model.addAttribute("orderList", list);
		model.addAttribute("orderProductCnt", list.size());
		return "shop/order";
	}
	
	
	@RequestMapping("/insertOrder.do")
	public String insertOrder(OrderVO vo, Model model, @ModelAttribute("member") MemberVO mvo) {
		vo.setId(mvo.getId());
		System.out.println(vo);
		String orderNum = orderService.insertOrders(vo);
		orderService.insertShip(orderNum);
		OrderVO ovo = orderService.getOrder(orderNum);
		System.out.println("OrderVO : " + ovo);
		model.addAttribute("order", ovo);
		return "shop/orderCompleted";
	}
	
	
	@RequestMapping("/productSale.do")
	public String getSaleList(OrderVO vo, Model model) {
		List<OrderVO> list = orderService.getSaleList(vo);
		List<OrderVO> list1 = orderService.getdaySaleList(vo);
		List<OrderVO> list2 = orderService.getday2SaleList(vo);
		List<OrderVO> list3 = orderService.getday3SaleList(vo);
		List<OrderVO> list4 = orderService.getday4SaleList(vo);
		List<OrderVO> list5 = orderService.getday5SaleList(vo);
		model.addAttribute("list", list);
		model.addAttribute("list1", list1);
		model.addAttribute("list2", list2);
		model.addAttribute("list3", list3);
		model.addAttribute("list4", list4);
		model.addAttribute("list5", list5);
		return "admin/product_sale";
	}
	
	@RequestMapping("/orderFail.do")
	public String orderFail(String errorMsg, Model model) {
		model.addAttribute("errorMsg", errorMsg);
		return "shop/orderFailed";
	}

}
