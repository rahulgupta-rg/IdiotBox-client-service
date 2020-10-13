package com.idiotBox.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.idiotBox.pojos.Admin;
import com.idiotBox.pojos.CommentList;
import com.idiotBox.pojos.Feedback;
import com.idiotBox.pojos.OtpAdminWrapper;
import com.idiotBox.pojos.Video;
import com.idiotBox.pojos.VideoList;
import com.idiotBox.service.VideoService;

@Controller
@RequestMapping("/admin")
public class AdminController {

	private String loginUrl = "http://localhost:7443/IdiotBoxServer/admin/login/{email}";
	private String registrationUrl = "http://localhost:7443/IdiotBoxServer/admin/register";
	private String viewAdminUrl = "http://localhost:7443/IdiotBoxServer/admin/viewAdmins";
	private String deleteUrl = "http://localhost:7443/IdiotBoxServer/admin/delete";
	private String addVideoUrl = "http://localhost:7443/IdiotBoxServer/admin/addVideo";
	private String addVideoDetailUrl = "http://localhost:7443/IdiotBoxServer/admin/addVideoDetail";
	private String getVideoUrl = "http://localhost:7443/IdiotBoxServer/admin/getVideo";
	private String verifyOtpUrl = "http://localhost:7443/IdiotBoxServer/admin/verifyOtp";
	private String getFeedbackUrl = "http://localhost:7443/IdiotBoxServer/admin/getFeedback";
	private String changePassword = "http://localhost:7443/IdiotBoxServer/admin/changePassword";


	@Autowired
	VideoService service;


	@GetMapping("/home")
	public String getHomePage(HttpSession session) {
		String role = (String) session.getAttribute("role");
		if (role == null) {
		} else if (role.equals("Admin")) {
			return "/admin/home";
		} else if (role.equals("User")) {
			return "redirect:/user/home";
		}
		return "redirect:/admin/login";
	}

	@GetMapping("/login")
	public String getLogin(HttpSession session) {
		String role = (String) session.getAttribute("role");
		if (role == null) {
		} else if (role.equals("Admin")) {
			return "redirect:/admin/home";
		} else if (role.equals("User")) {
			return "redirect:/user/home";
		}
		return "/admin/login";
	}

	@PostMapping("/login")
	public String showLoginForm(@RequestParam String email, @RequestParam String password, HttpSession session,
			RedirectAttributes flashMap, RestTemplate template) {
		ResponseEntity<OtpAdminWrapper> entity = template.postForEntity(loginUrl, password, OtpAdminWrapper.class,
				email);
		HttpStatus status = entity.getStatusCode();
		if (status == HttpStatus.OK) {
			OtpAdminWrapper wrapper = (OtpAdminWrapper) entity.getBody();
			session.setAttribute("receivedOtpFromRestForLogin", wrapper.getOtp());
			session.setAttribute("requestedAdminLogin", wrapper.getAdmin());
			return "/admin/otpLogin";
		} else {
			flashMap.addFlashAttribute("status", "Login Failed");
			return "redirect:/admin/login";
		}
	}

	@PostMapping("/otpLogin")
	public String verifyOtpLogin(@RequestParam String otp, HttpSession session, RedirectAttributes flashMap,
			RestTemplate template) {
		String receivedOtp = (String) session.getAttribute("receivedOtpFromRestForLogin");
		Admin admin = (Admin) session.getAttribute("requestedAdminLogin");
		session.removeAttribute("receivedOtpFromRestForLogin");
		session.removeAttribute("requestedAdminLogin");
		if (otp.equals(receivedOtp)) {
			session.setAttribute("loggedAdmin", admin);
			session.setAttribute("role", "Admin");
			return "redirect:/admin/home";
		} else {
			flashMap.addFlashAttribute("errorMessage", "You have entered invalid otp");
			return "redirect:/admin/login";
		}
	}

	@GetMapping("/register")
	public String showRegisterForm(Admin admin, HttpSession session) {
		String role = (String) session.getAttribute("role");
		if (role == null) {
		} else if (role.equals("Admin")) {
			return "/admin/register";
		} else if (role.equals("User")) {
			return "redirect:/user/home";
		}
		return "/admin/register";
//		return "redirect:/admin/index";
	}

	@PostMapping("/register")
	public String postRegisterForm(Admin admin, HttpSession session, RedirectAttributes flashMap,
			RestTemplate template) {
		ResponseEntity<String> entity = template.postForEntity(registrationUrl, admin, String.class);
		HttpStatus status = entity.getStatusCode();
		if (status == HttpStatus.OK) {
			String otp = entity.getBody();
			session.setAttribute("requestedAdminForRegister", admin);
			session.setAttribute("receivedOtpFromRestForRegister", otp);
			return "redirect:/admin/otp";
		
		}
		String message = entity.getBody();
		flashMap.addFlashAttribute("errorMessage", message);
		return "redirect:/admin/register";
	}

	@GetMapping("/otp")
	public String getOtpPage(HttpSession session) {
		String role = (String) session.getAttribute("role");
		Admin admin = (Admin) session.getAttribute("requestedAdminForRegister");
		if (role == null) {
			if (admin != null) {
				return "/admin/otp";
			}
		} else if (role.equals("User")) {
			return "redirect:/user/home";
		} else if (role.equals("Admin")) {
			return "redirect:/admin/home";
		}
		return "redirect:/admin/login";
	}
	@GetMapping("/view")
	public String vm(RestTemplate template,Model map, HttpSession session)
	{
		String role = (String) session.getAttribute("role");
		if (role == null || !role.equals("Admin")) {
			return "redirect:/user/login";
		}
		VideoList ls= template.getForObject("http://localhost:7443/IdiotBoxServer/vcr/getvideo", VideoList.class); 
		map.addAttribute("list", ls.getLs() );
		System.out.println("Clei---"+ls.getLs());
		return "/admin/view";
	}

	@PostMapping("/otp")
	public String verifyOtpRegister(@RequestParam String otp, HttpSession session, RedirectAttributes flashMap,
			RestTemplate template) {
		String otpFromServer = (String) session.getAttribute("receivedOtpFromRestForRegister");
		Admin admin = (Admin) session.getAttribute("requestedAdminForRegister");
		session.removeAttribute("receivedOtpFromRestForRegister");
		session.removeAttribute("requestedAdminForRegister");
		if (otp.equals(otpFromServer)) {
			ResponseEntity<String> entity = template.postForEntity(verifyOtpUrl, admin, String.class);
			HttpStatus status = entity.getStatusCode();
			if (status == HttpStatus.OK) {
				String str = entity.getBody();
				flashMap.addFlashAttribute("message", str);
				return "/admin/home";
			} else {
				String str = entity.getBody();
				flashMap.addFlashAttribute("errorMessage", str);
				return "redirect:/admin/register";
			}
		} else {
			flashMap.addFlashAttribute("errorMessage", "Otp not valid");
			return "/admin/register";
		}
	}

	@GetMapping("/viewAdmins")
	public String getAllAdmins(Model map, HttpSession session, RestTemplate template) {
		String role = (String) session.getAttribute("role");
		if (role == null || !role.equals("Admin")) {
			return "redirect:/user/index";
		}
		Admin admin = (Admin) session.getAttribute("loggedAdmin");
		ResponseEntity<?> entity = template.postForEntity(viewAdminUrl, admin, List.class);
		HttpStatus status = entity.getStatusCode();
		if (status == HttpStatus.OK) {
			List<Admin> adminList = (List<Admin>) entity.getBody();
			map.addAttribute("list", adminList);
		} else {
			map.addAttribute("errorMessage", "Can't View Admin List");
		}
		return "/admin/viewAdmins";
	}

	@GetMapping("/delete")
	public String deleteAdmin(@RequestParam Integer adminId, RedirectAttributes flashMap, RestTemplate template,
			HttpSession session) {
		String role = (String) session.getAttribute("role");
		if (role == null) {
		} else if (role.equals("Admin")) {
			ResponseEntity<String> entity = template.postForEntity(deleteUrl, adminId, String.class);
			HttpStatus status = entity.getStatusCode();
			if (status == HttpStatus.OK) {
				String str = entity.getBody();
				flashMap.addFlashAttribute("status", str);
			} else {
				String str = entity.getBody();
				flashMap.addFlashAttribute("status", str);
			}
		}
		return "redirect:/admin/viewAdmins";
	}

	@GetMapping("/comments")
	public String getComments(HttpSession session) {
		String role = (String) session.getAttribute("role");
		if (role == null || !role.equals("Admin")) {
			return "redirect:/user/index";
		}
		return "/admin/comments";
	}

	@PostMapping("/comments")
	public String postComments(@RequestParam("videoname") String vnm, RestTemplate template, Model map) {
		CommentList ls= template.getForObject("http://localhost:7443/IdiotBoxServer/vcr/getcomments/"+vnm, CommentList.class); 
		map.addAttribute("list", ls.getLs() );
		System.out.println("Clei---"+ls.getLs());
		;
		return "/admin/viewcomments";
	}

	@GetMapping("/logout")
	public String logoutStudent(Model map, HttpSession session, HttpServletRequest request,
			HttpServletResponse response) {
		String role = (String) session.getAttribute("role");
		if (role == null) {
		} else if (role.equals("Admin")) {
			map.addAttribute("admin", session.getAttribute("loggedAdmin"));
			session.invalidate();
			response.setHeader("refresh", "5;url=" + request.getContextPath() + "/admin/login/");
			return "/admin/logout";
		} else if (role.equals("User")) {
			return "redirect:/user/home";
		}
		return "redirect:/admin/index";
	}

	@GetMapping("/addVideo")
	public String showVideoRegistrationPage(Video video, HttpSession session) {
		String role = (String) session.getAttribute("role");
		if (role != null && role.equals("Admin")) {
			return "/admin/addVideo";
		}
		return "/admin/addVideo";
	}

	@PostMapping("/addVideo")
	public String registerVideo(@ModelAttribute Video video, @RequestParam MultipartFile uploadedFile,
			HttpSession session, RestTemplate template) throws RestClientException, IOException {
		HttpEntity<MultiValueMap<String, Object>> requestEntity = service.fileToResource(uploadedFile, video);
		System.out.println(uploadedFile.getOriginalFilename());
		ResponseEntity<String> videoEntity = template.postForEntity(addVideoUrl, requestEntity, String.class);
		if (videoEntity.getStatusCode() == HttpStatus.OK) {
			String videoPath = videoEntity.getBody();
			video.setVideoPath(videoPath);
			ResponseEntity<Integer> detailEntity = template.postForEntity(addVideoDetailUrl, video, Integer.class);
			System.out.println("Video : " + videoEntity.getBody() + " Detail : " + detailEntity.getBody());
		}
		session.setAttribute("Message", videoEntity.getBody());
		return "/admin/home";
	}

	@GetMapping("/getVideo")
	public String showGetIdForm(HttpSession session) {
		String role = (String) session.getAttribute("role");
		if (role != null && role.equals("Admin")) {
			return "/admin/getVideo";
		} else {
			return "redirect:/admin/login";
		}
	}

	@PostMapping("/getVideo")
	public String getVideoByID(@RequestParam int VideoID, Model map, RestTemplate template) {
		ResponseEntity<File> entity = template.postForEntity(getVideoUrl, VideoID, File.class);
		map.addAttribute("theFile", entity.getBody().getName());
		return "/admin/seeVideo";
	}

	@GetMapping("/getFeedback")
	public String getFeedbackPage(HttpSession session, RestTemplate template, Model map) {
		String role = (String) session.getAttribute("role");
		if (role == null || !role.equals("Admin")) {
		} else {
			ResponseEntity<List> entity = null;
			entity = template.getForEntity(getFeedbackUrl, List.class);
			HttpStatus status = entity.getStatusCode();
			if (status == HttpStatus.OK) {
				List<Feedback> feedback = (List<Feedback>) entity.getBody();
				map.addAttribute("listFeedback", feedback);
			} else {
				map.addAttribute("status", "No feedback");
			}
			return "/admin/feedback";
		}
		return "redirect:/user/login";
	}
	@GetMapping("/changePassword")
	public String getChangePasswordPage()
	{
		return "/admin/changePassword";
	}
	@PostMapping("/changePassword")
	public String processChangePasswordPage(@RequestParam String oldPassword, @RequestParam String newPassword
		,RestTemplate template,HttpSession session,RedirectAttributes map)
	{
		Admin admin = (Admin)session.getAttribute("loggedAdmin");
		System.out.println(admin+" "+admin.getPassword()+" "+oldPassword+" "+newPassword) ;
		String updatedStatus = "";
		if(admin.getPassword().equals(oldPassword))
		{
			admin.setPassword(newPassword);
			ResponseEntity<String> entity = template.postForEntity(changePassword,admin,String.class);
			System.out.println("Here");
			HttpStatus httpStatus = entity.getStatusCode();
			if (httpStatus  == HttpStatus.OK) 
			{
				updatedStatus = (String)entity.getBody();
				System.out.println(updatedStatus);
			} 
			map.addFlashAttribute("status",updatedStatus);
		}
		else
			map.addFlashAttribute("status","Invalid Password");
		return "redirect:/admin/changePassword";
	}


}