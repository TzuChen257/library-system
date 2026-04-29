package util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import model.Messages;
import service.impl.MessagesServiceImpl;

public class MessageShow {
	//f
	private Messages msg;
	private int personalMsgNum;
	//c
	public MessageShow(Messages msg, int personalMsgNum) {
		super();
		this.msg = msg;
		this.personalMsgNum = personalMsgNum;
	}
	//m
	public Messages getMsg() {
		return msg;
	}
	public void setMsg(Messages msg) {
		this.msg = msg;
	}
	public int getPersonalMsgNum() {
		return personalMsgNum;
	}
	public void setPersonalMsgNum(int personalMsgNum) {
		this.personalMsgNum = personalMsgNum;
	}
	//design
	public List<MessageShow> allByReader(String reader_id)
	{
		List<Messages> msgs=new MessagesServiceImpl().inbox(reader_id)
				.stream().sorted().collect(Collectors.toList());
		List<MessageShow> list=new ArrayList<>();
		int num=1;
		for(int i=0;i<msgs.size();i++)
		{
			list.add(new MessageShow(msgs.get(i),num));
		}
		Collections.reverse(list);		
		return list;
	}	
}
