package net.Server;

public class MessageQueue {
	
	private String from_Name;
	private String to_Name;
	private String[] msgs;
	private int head;
	private int tail;
	private int length = 100;
	public MessageQueue(String from, String to) {
		msgs = new String[length];
		from_Name = from;
		to_Name = to;
		for(int i = 0;i<length;i++) {
			msgs[i] = "";
		}
		head = 0;
		tail = 0;
	}
	//������޲�����length������Ϣ
	//strΪ�������ı���Ϣ���Ϸ���ʱ��---#����
	public void AddMsg(String str) {
		if(tail < length) {
			msgs[tail++] = str;
		}
		else {
			if(head == length) {
				head = 0;
			}
			msgs[head++] = str;
		}
	}
	//������ȡ���������Ϣ
	 public String[] getMsgs() {
		 return msgs;
	 }
	 public String getFName() {
		 return from_Name;
	 }
	 public String getTName() {
		 return to_Name;
	 }
}
