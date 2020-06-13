package network.server3.controller;

import network.server3.vo.Message;

public interface Controller {
	abstract void dataHandler(String jsonData);
	abstract void dataHandler(Message data);
}
