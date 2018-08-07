package io.oacy.pluviophile.repository;

import java.io.RandomAccessFile;
import java.util.Arrays;

import io.oacy.pluviophile.domain.UserInfo;

/**
 * DAO 数据连接对象 Data Access Object 是数据持久层中的类的总称
 * 
 * DAO的主要工作是对数据进行持久化操作.(对数据做实际的 增删改查). DAO与业务逻辑层之间互相传递数据时是以对象的形式传递的.
 * 
 * DAO负责将业务逻辑层传递过来的对象中的所有数据保存起来, 也负责将数据再转换为对象传递给业务逻辑层.
 * 
 * 
 * UserInfoDAO负责对用户数据做增删改查操作.
 */
public class UserInfoDAO {
	/**
	 * 修改用户信息
	 * @param userinfo
	 * @return
	 */
	public boolean update(UserInfo userinfo){
		/*
		 * 首先根据userinfo中的username找到user.dat
		 * 文件中的该用户记录,然后将指针移动到密码的
		 * 位置,开始将密码,昵称,手机号重写写入文件覆盖
		 * 以前的数据完成修改操作.
		 */
		return false;
	}
	
	/**
	 * 保存用户信息
	 * @param userinfo
	 * @return true:保存成功   false:保存失败
	 */
	public boolean save(UserInfo userinfo){
		/*
		 * 使用RandomAccessFile写user.dat文件
		 * 先将指针移动到文件最后,然后写入116字节的内容
		 * 将给定的UserInfo对象表示的用户信息保存.
		 */
		try (
			RandomAccessFile raf = new RandomAccessFile("user.dat","rw");	
		){
			raf.seek(raf.length());
			
			writeString(raf,userinfo.getUsername(),32);
			writeString(raf,userinfo.getPassword(),32);
			writeString(raf,userinfo.getNickname(),32);
			writeString(raf,userinfo.getPhonenumber(),20);
			
			return true;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 根据给定的用户名查找该用户信息
	 * @param username
	 * @return	若返回值为null表示没有此用户.
	 */
	public UserInfo findByUsername(String username){
		try (
			RandomAccessFile raf 
				= new RandomAccessFile("user.dat","r");	
		){
			for(int i=0;i<raf.length()/116;i++){
				raf.seek(i*116);
				String name = readString(raf,32);
				if(name.equals(username)){
					String password = readString(raf,32);
					String nickname = readString(raf,32);
					String phonenumber = readString(raf,20);
					UserInfo userInfo = new UserInfo(username, password, nickname, phonenumber);
					return userInfo;
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	private void writeString(RandomAccessFile raf,String str,int len){
		try {
			byte[] data = str.getBytes("UTF-8");
			data = Arrays.copyOf(data, len);
			raf.write(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String readString(RandomAccessFile raf,int len){
		try {
			byte[] data = new byte[len];
			raf.read(data);
			String str = new String(data,"UTF-8").trim();
			return str;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) {
		UserInfoDAO dao = new UserInfoDAO();
		UserInfo userinfo = dao.findByUsername("zephyr");
		System.out.println(userinfo);
	}
}

