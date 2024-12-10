package kr.just.tj.controller;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import kr.just.tj.service.UserService;
import lombok.extern.slf4j.Slf4j;

@Controller
@Configuration
@Slf4j
public class AdminController {
	
	@Autowired
	private UserService userService;

	ResourceLoader resourceLoader;
	
	// 관리자 페이지 접근
	@GetMapping("/admin")
	public String admin() {
		return "admin";
	}
	// 유저목록 페이징
	@GetMapping(value =  "/admin/users")
	public String adminUsers(@RequestParam (required = false, name = "field") String field,
						@RequestParam (required = false, name = "search") String search,
						Model model) {
		model.addAttribute("field", field);
		model.addAttribute("search", search);
		model.addAttribute("newLine", "\n");
		model.addAttribute("br", "<br>");
		return "admin-users";
	}
	
	// 회원 상세 조회
	@GetMapping(value = "/admin/user/details")
	public String adminUserDetail(Integer user_id, Model model) {
		model.addAttribute("user_id",user_id);
		return "admin-userdetails";
	}
		// 토스트 에디터 이미지 처리
		@ResponseBody
		@RequestMapping(value = "/image_upload.do", method = RequestMethod.POST)
		public String imageUpload(@RequestParam("image")MultipartFile multipartFile,
								  HttpServletRequest request) {
			String projectDir = System.getProperty("user.dir");
			String filePath = projectDir +  "/src/main/resources/static/images/products/toast";
			File file = new File(filePath);
			if(!file.exists()) file.mkdirs();
			String fileName = multipartFile.getOriginalFilename();
			int lastIndex = fileName.lastIndexOf(".");
			String ext = fileName.substring(lastIndex, fileName.length());
			String newFileName = LocalDate.now() + "_" + System.currentTimeMillis() + ext;

			try {
				File image = new File(filePath, newFileName);
				multipartFile.transferTo(image);
			} catch (IllegalStateException | IOException e) {
				e.printStackTrace();
			}
			return "/images/products/toast/" + newFileName;
		}
}