package com.idiotBox.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.idiotBox.pojos.OtpUserWrapper;
import com.idiotBox.pojos.User;
import com.idiotBox.service.PasswordHasher;

@Controller
@RequestMapping("/user")
public class UserController {
	private String loginUrl = "http://localhost:7443/IdiotBoxServer/user/login";
	private String registrationUrl = "http://localhost:7443/IdiotBoxServer/user/register";
	private String verifyOtpUrl = "http://localhost:7443/IdiotBoxServer/user/verifyOtp";
	private String updateTwoWayAuthStatusUrl = "http://localhost:7443/IdiotBoxServer/user/twoWayAuthStatus";
	private String changePasswordUrl = "http://localhost:7443/IdiotBoxServer/user/changePassword";
	private String forgetPasswordUrl = "http://localhost:7443/IdiotBoxServer/user/forgetPassword";
	private String sendOtpToEmailUrl = "http://localhost:7443/IdiotBoxServer/user/sendOtpToEmail";
	private String sendFeedbackUrl = "http://localhost:7443/IdiotBoxServer/user/sendEmail";

	@Autowired
	PasswordHasher hasher;
	@GetMapping("/*")
	public String gd() {
		
		return "/user/saa";
	}
	
	@GetMapping("/login")
	public String getLogin(User user, HttpSession session) {
		String role = (String) session.getAttribute("role");
		if (role == null) {
		} else if (role.equals("User")) {
			return "redirect:/user/login";
		} else if (role.equals("Admin")) {
			return "redirect:/admin/login";
		}
		return "/user/login";
	}
	
	

	@PostMapping("/login")
	public String postLogin(User user, RedirectAttributes flashMap, HttpSession session, RestTemplate template) {
		System.out.println("user details "+user);
		ResponseEntity<OtpUserWrapper> entity = template.postForEntity(loginUrl, user, OtpUserWrapper.class);
		HttpStatus status = entity.getStatusCode();
		if (status == HttpStatus.OK) {
			OtpUserWrapper wrapper = entity.getBody();
			if (wrapper.getOtp() != null) {
				session.setAttribute("receivedOtpFromRestForLogin", wrapper.getOtp());
				session.setAttribute("requestedUserForLogin", wrapper.getUser());
				return "redirect:/user/otpLogin";
			} else {
				session.setAttribute("loggedUser", (User)wrapper.getUser());
				session.setAttribute("role", "User");
				return "redirect:/user/home";
			}
		} else {
			flashMap.addAttribute("mesg", "Invalid Login");
			return "redirect:/user/login";
		}
	}

	@GetMapping("/otpLogin")
	public String showOtpLoginPage(HttpSession session) {
		String role = (String) session.getAttribute("role");
		if (role == null) {
		} else if (role.equals("User")) {
			return "redirect:/user/login";
		} else if (role.equals("Admin")) {
			return "redirect:/admin/login";
		}
		return "/user/otpLogin";
	}

	@PostMapping("/otpLogin")
	public String verifyOtpLogin(@RequestParam String otp, HttpSession session, RedirectAttributes flashMap) {
		String receivedOtp = (String) session.getAttribute("receivedOtpFromRestForLogin");
		if (otp.equals(receivedOtp)) {
			session.setAttribute("loggedUser", (User)session.getAttribute("requestedUserForLogin"));
			session.setAttribute("role", "User");
			session.removeAttribute("receivedOtpFromRestForLogin");
			session.removeAttribute("requestedUserForLogin");
			return "redirect:/user/home";
		} else {
			flashMap.addFlashAttribute("errorMessage", "You have entered invalid otp");
			return "/user/otpLogin";
		}
	}

	@GetMapping("/home")
	public String showHomePage(HttpSession session) {
		String role = (String) session.getAttribute("role");
		if (role == null) {
		} else if (role.equals("Admin")) {
			return "redirect:/admin/home";
		} else if (role.equals("User")) {
			return "/user/home";
		}
		return "redirect:/user/login";
	}

	@GetMapping("/register")
	public String getRegistration(User user, HttpSession session) {
		String role = (String) session.getAttribute("role");
		if (role == null) {
		} else if (role.equals("User")) {
			return "redirect:/user/login";
		} else if (role.equals("Admin")) {
			return "redirect:/admin/index";
		}
		return "/user/register";
	}

	@PostMapping("/checkPlans")
	public String getCheckPlansPage(User user, HttpSession session) {
		session.setAttribute("requestedUserForRegister", user);
		return "/user/checkPlans";
	}

	@PostMapping("/register")
	public String processRegistrationForm(@RequestParam String plan, HttpSession session, RedirectAttributes flashMap,
			RestTemplate template) {
		User user = (User) session.getAttribute("requestedUserForRegister");
		user.setTwoFactAuthStatus("Active");
		session.setAttribute("plan", plan);
		System.out.println("Hey in IdiotBox");
		ResponseEntity<String> entity = template.postForEntity(registrationUrl, user, String.class);
		HttpStatus status = entity.getStatusCode();
		if (status == HttpStatus.OK) 
		{
			String otp = entity.getBody();
			System.out.println("OTP "+otp);
			session.setAttribute("receivedOtpFromRestForRegister", otp);
			return "/user/otp";
		} else {
			String message = entity.getBody();
			flashMap.addFlashAttribute("errorMessage", message);
			return "redirect:/user/register";
		}
	}

	@PostMapping("/otp")
	public String verifyOtpRegister(@RequestParam String otp, HttpSession session, RedirectAttributes flashMap,
			RestTemplate template) {
		String otpFromServer = (String) session.getAttribute("receivedOtpFromRestForRegister");
		System.out.println("OTP from server"+otpFromServer);
		if (otp.equals(otpFromServer)) {
			User user = (User) session.getAttribute("requestedUserForRegister");
			System.out.println("Hey");
			ResponseEntity<String> entity = template.postForEntity(verifyOtpUrl, user, String.class);
			HttpStatus status = entity.getStatusCode();
			session.removeAttribute("receivedOtpFromRestForRegister");
			session.removeAttribute("requestedUserForRegister");
			if (status == HttpStatus.OK) {
				String str = entity.getBody();
				user.setTwoFactAuthStatus("Active");
				flashMap.addFlashAttribute("message", str);
				String plan = (String) session.getAttribute("plan");
				System.out.println("Hello");
				// To:Do -> Redirect to .Net Payment Service
				//return "redirect:http://localhost:63732/transaction.aspx";
				return "redirect:http://localhost:63732/transaction.aspx?amount="+plan;
			} else {
				String str = entity.getBody();
				flashMap.addFlashAttribute("errorMessage", str);
				return "redirect:/user/register";
			}
		} else {
			flashMap.addFlashAttribute("errorMessage", "Otp not valid");
			return "/user/otp";
		}
	}

	@GetMapping("/settings")
	public String showSettingsPage(HttpSession session) {
		String role = (String) session.getAttribute("role");
		if (role == null) {
		} else if (role.equals("User")) {
			return "/user/settings";
		} else if (role.equals("Admin")) {
			return "redirect:/admin/settings";
		}
		return "redirect:/user/login";
	}

	@GetMapping("/twofactorauth")
	public String getTwoWayAuthPage(HttpSession session, Model map) {
		String role = (String) session.getAttribute("role");
		if (role == null) {
		} else if (role.equals("User")) {
			User user = (User) session.getAttribute("loggedUser");
			if (user.getTwoFactAuthStatus().equals("Active"))
				map.addAttribute("status", "Activated");
			else
				map.addAttribute("status", "Deactivated");
			return "/user/twofactauth";
		}
		return "/user/twofactauth";
	}

	@PostMapping("/twofactorauth")
	public String processTwoWayAuthPage(@RequestParam String status, Model map, RestTemplate template,
			HttpSession session) {
		User user = (User) session.getAttribute("loggedUser");
		String updatedStatus = "";
		user.setTwoFactAuthStatus(status);
		ResponseEntity<String> entity = template.postForEntity(updateTwoWayAuthStatusUrl, user, String.class);
		HttpStatus httpStatus = entity.getStatusCode();
		if (httpStatus == HttpStatus.OK) {
			updatedStatus = (String) entity.getBody();
			session.setAttribute("loggedUser", user);
		}
		map.addAttribute("newStatus", updatedStatus);
		return "/user/twofactauth";
	}

	@GetMapping("/changePassword")
	public String getChangePasswordPage(HttpSession session) {
		String role = (String) session.getAttribute("role");
		if (role == null) {
		} else if (role.equals("User")) {
			return "/user/changePassword";
		} else if (role.equals("Admin")) {
			return "redirect:/admin/changePassword";
		}
		return "redirect:/user/index";
	}

	@PostMapping("/changePassword")
	public String processChangePasswordPage(@RequestParam String oldPassword, @RequestParam String newPassword,
			RestTemplate template, HttpSession session, RedirectAttributes flashMap) {
		User user = (User) session.getAttribute("loggedUser");
		String updatedStatus = "";
		if (hasher.matchHashedString(oldPassword, user.getPassword())) {
			ResponseEntity<String> entity = template.postForEntity(changePasswordUrl, user, String.class);
			HttpStatus status = entity.getStatusCode();
			if (status == HttpStatus.OK) {
				updatedStatus = (String) entity.getBody();
				session.setAttribute("loggedUser", user);
			}
			flashMap.addFlashAttribute("status", updatedStatus);
		} else
			flashMap.addFlashAttribute("status", "Invalid Password");
		return "redirect:/user/changePassword";
	}

	@GetMapping("/forgetPassword")
	public String getForgetPasswordPage(HttpSession session) {
		String role = (String) session.getAttribute("role");
		if (role == null) 
		{
			
		} else if (role.equals("User")) {
			return "/user/index";
		} else if (role.equals("Admin")) {
			return "redirect:/admin/index";
		}
		return "/user/forgetPassword";
	}

	@PostMapping("/forgetPassword")
	public String processForgetPasswordPage(@RequestParam String userEmail, RestTemplate template, HttpSession session,
			RedirectAttributes flashMap) {
		ResponseEntity<OtpUserWrapper> entity = template.postForEntity(forgetPasswordUrl, userEmail,
				OtpUserWrapper.class);
		HttpStatus status = entity.getStatusCode();
		System.out.println("hey");
		if (status == HttpStatus.OK) {
			OtpUserWrapper wrapper = (OtpUserWrapper) entity.getBody();
			session.setAttribute("receivedOtpFromRestForChangePassword", wrapper.getOtp());
			session.setAttribute("requestedUserForLogin", wrapper.getUser());
			return "redirect:/user/otpForgetPassword";
		}
		flashMap.addFlashAttribute("error_msg", "Can't Send Requested Link");
		return "/user/forgetPassword";
	}

	@GetMapping("/otpForgetPassword")
	public String getVerifyOtpForgetPasswordPage(HttpSession session) {
		String role = (String) session.getAttribute("role");
		User user = (User) session.getAttribute("requestedUserForLogin");
		if (role == null) {
		} else if (role.equals("User")) {
			return "redirect:/user/home";
		} else if (role.equals("Admin")) {
			return "redirect:/admin/home";
		} else if (user != null) {
			return "/user/otpForgetPassword";
		}
		return "/user/otpForgetPassword";
	}

	@PostMapping("/otpForgetPassword")
	public String processVerifyOtpForgetPasswordPage(@RequestParam String otp, HttpSession session,
			RestTemplate template, RedirectAttributes flashMap) {
		String otpFromRest = (String) session.getAttribute("receivedOtpFromRestForChangePassword");
		User user = (User) session.getAttribute("requestedUserForLogin");
		session.removeAttribute("receivedOtpFromRestForChangePassword");
		session.removeAttribute("requestedUserForLogin");
		if (otpFromRest.equals(otp)) {
			ResponseEntity<String> entity = template.postForEntity(sendOtpToEmailUrl, user, String.class);
			HttpStatus status = entity.getStatusCode();
			if (status == HttpStatus.OK) {
				String response = (String) entity.getBody();
				flashMap.addFlashAttribute("Message", response);
				return "redirect:/user/login";
			} else {
				flashMap.addFlashAttribute("Message", "Password Can't Be Updated");
				return "redirect:/user/login";
			}
		} else {
			flashMap.addFlashAttribute("errorMessage", "Entered otp is not valid");
			return "/user/otpForgetPassword";
		}
	}

	@GetMapping("/sendFeedback")
	public String getSendEmailPage(HttpSession session) {
		String role = (String) session.getAttribute("role");
		if (role == null) {
		} else if (role.equals("User")) 
		{
			return "/user/sendFeedback";
		} else if (role.equals("Admin")) {
			return "redirect:/admin/home";
		}
		return "redirect:/user/home";
	}

	@PostMapping("/sendFeedback")
	public String processSendEmailPage(@RequestParam String subject, @RequestParam String body, RestTemplate template,
			HttpSession session, RedirectAttributes flashMap) {
		User user = (User) session.getAttribute("loggedUser");
		String fromEmail = user.getEmail();
		List<String> list = new ArrayList<String>();
		list.add(fromEmail);
		list.add(subject);
		list.add(body);
		ResponseEntity<String> entity = template.postForEntity(sendFeedbackUrl, list, String.class);
		HttpStatus status = entity.getStatusCode();
		if (status == HttpStatus.OK) {
			String response = (String) entity.getBody();
			flashMap.addFlashAttribute("emailResponse", response);
		}
		return "redirect:/user/sendFeedback";
	}

	@GetMapping("/logout")
	public String logoutStudent(Model map, HttpSession session, HttpServletRequest req, HttpServletResponse res) {
		String role = (String) session.getAttribute("role");
		if (role == null) {
		} else if (role.equals("Admin")) {
			return "redirect:/admin/login";
		} else if (role.equals("User")) {
			map.addAttribute("user", session.getAttribute("loggedUser"));
			session.invalidate();
			res.setHeader("refresh", "5;url=" + req.getContextPath() + "/user/login");
			return "/admin/logout";
		}
		return "redirect:/user/login";
	}
	@InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        sdf.setLenient(true);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(sdf, true));
    }
}