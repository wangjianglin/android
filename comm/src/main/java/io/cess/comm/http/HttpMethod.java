package io.cess.comm.http;

public enum HttpMethod {
	//GET,POST,PUT,HEAD,DELETE;
	GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;

	@Override
	public String toString() {
		switch (this) {
			case GET:
				return "GET";
			case HEAD:
				return "HEAD";
			case POST:
				return "POST";
			case PUT:
				return "PUT";
			case PATCH:
				return "PATCH";
			case DELETE:
				return "DELETE";
			case OPTIONS:
				return "OPTIONS";
			case TRACE:
				return "TRACE";
		}
		return "";
	}

}