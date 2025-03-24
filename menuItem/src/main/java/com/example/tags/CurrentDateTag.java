package com.example.tags;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.tagext.SimpleTagSupport;


public class CurrentDateTag extends SimpleTagSupport {
      
	@Override
    public void doTag() throws JspException, IOException {
    	// TODO Auto-generated method stub
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	Date currentDate = new Date();
    	String displayDate = format.format(currentDate);
    	getJspContext().getOut().write(displayDate);
    }
}
