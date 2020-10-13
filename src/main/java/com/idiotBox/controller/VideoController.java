package com.idiotBox.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.idiotBox.pojos.Video;
import com.idiotBox.pojos.CommentList;
import com.idiotBox.pojos.VideoList;

@Controller
@RequestMapping("/video")
public class VideoController {
	
	@GetMapping("/viewcom/{a}")
	public String gac(RestTemplate t,ModelMap map,@PathVariable String a)
	{
		
		CommentList ls= t.getForObject("http://localhost:7443/IdiotBoxServer/vcr/getcomments/"+a, CommentList.class);
		System.out.println(ls.getLs());
		map.addAttribute("list", ls.getLs());
		return "/video/viewcom";}
	
	@GetMapping("/get/{id}")  //new added to redirect to movie page
	public String nqam(@PathVariable String id, RestTemplate est, Model map)
	{
		Video c=est.getForObject("http://localhost:7443/IdiotBoxServer/vcr/get/"+id, Video.class);
		map.addAttribute("list",c);
		VideoList ls= est.getForObject("http://localhost:7443/IdiotBoxServer/vcr/getadvrecom/"+id,VideoList.class);
		map.addAttribute("reclist", ls.getLs());
		
		return "/video/viewsingle";
	}
	
	@GetMapping("/view")
	public String vm(RestTemplate template,Model map)
	{
		VideoList ls= template.getForObject("http://localhost:7443/IdiotBoxServer/vcr/getvideo", VideoList.class); //here lies the problem 
		map.addAttribute("list", ls.getLs() );
		System.out.println("Clei---"+ls.getLs());
		return "/video/view";
	}
	
	@GetMapping("/searchn")
	public String vm(RestTemplate template,Model map,@RequestParam String s)
	{
		VideoList ls= template.getForObject("http://localhost:7443/IdiotBoxServer/search/byname/"+s, VideoList.class); //here lies the problem 
		map.addAttribute("list", ls.getLs() );
		System.out.println("Clei---"+ls.getLs());
		return "/video/view";
	}
	
	@GetMapping("/searcha")
	public String vma(RestTemplate template,Model map,@RequestParam String s)
	{
		System.out.println("Hey data "+s);
		VideoList ls= template.getForObject("http://localhost:7443/IdiotBoxServer/search/byact/"+s, VideoList.class); //here lies the problem 
		map.addAttribute("list", ls.getLs() );
		System.out.println("Clei---"+ls.getLs());
		return "/video/view";
	}
	
	@GetMapping("/searchd/{s}")
	public String vmd(RestTemplate template,Model map,@PathVariable String s)
	{
		VideoList ls= template.getForObject("http://localhost:7443/IdiotBoxServer/search/bydir/"+s, VideoList.class); //here lies the problem 
		map.addAttribute("list", ls.getLs() );
		System.out.println("Clei---"+ls.getLs());
		return "/video/view";
	}
	
	@GetMapping("/searchc/{s}")
	public String vmc(RestTemplate template,Model map,@PathVariable String s)
	{
		VideoList ls= template.getForObject("http://localhost:7443/IdiotBoxServer/search/bycat/"+s, VideoList.class); //here lies the problem 
		map.addAttribute("list", ls.getLs() );
		System.out.println("Clei---"+ls.getLs());
		return "/video/view";
	}
	
	
}
