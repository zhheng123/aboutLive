package com.zhibo;
public class Minute {

	public static void main(String[] args) {
		StringBuffer buff = new StringBuffer();

			long dur=5400l;
			long  h=dur/3600000;
			if(h==0){
				
			}else if(h<10){
				buff.append("0").append(h).append(":");
			}else{
				buff.append(h).append(":");
			}
			long m =  (dur-h*3600000)/60000;
			if(m<10){
				buff.append("0").append(m).append(":");
			}else{
				buff.append(m).append(":");
			}
			 m = ( (dur-h*3600000)%60000)/1000;
			if(m<10){
				buff.append("0").append(m);
			}else{
				buff.append(m);
			}

		System.out.println(buff.toString());

	}

}
