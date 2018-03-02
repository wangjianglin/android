package io.cess.comm.http;

public enum HttpMethod {
	GET,POST,PUT,HEAD,DELETE;

	@Override
	public String toString() {
		switch(this){
		case GET:
			return "GET";
		case POST:
			return "POST";
		case PUT:
			return "PUT";
		case HEAD:
			return "HEAD";
		case DELETE:
			return "DELETE";
		}
		return "";
	}
	
}
