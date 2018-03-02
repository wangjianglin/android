package io.cess.comm.tcp;

public interface SessionListener {
	void Create(Session session);

	void Destory(Session session);
}
