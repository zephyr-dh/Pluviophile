package io.oacy.pluviophile.domain;

/**
 * VO  value object 值对象
 * 值对象的作用是用一个对象来保存一组数据.
 * 
 * 比如一个用户有很多项信息:用户名,密码,昵称...
 * 就可以设计一个UserInfo类,用这个类的每个实例表示一个
 * 具体的用户数据.
 * 
 * 值对象通常没有业务逻辑功能,仅用来保存一组数据使用.
 * 
 * @author zephyr
 *
 */
public class UserInfo {
	private String username;
	private String password;
	private String nickname;
	private String phonenumber;
	
	public UserInfo(){
		
	}

	public UserInfo(String username, String password, String nickname, String phonenumber) {
		super();
		this.username = username;
		this.password = password;
		this.nickname = nickname;
		this.phonenumber = phonenumber;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}
	
	public String toString(){
		return username+","+password+","+nickname+","+phonenumber;
	}
}
