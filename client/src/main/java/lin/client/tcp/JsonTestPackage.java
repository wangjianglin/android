package lin.client.tcp;

@JsonPath(path = "/json/test/")
public class JsonTestPackage extends JsonPackage {
	private String data = null;

	public JsonTestPackage() {
		data = "test.";
	}

	@JsonParamsType(type = String.class)
	@Override
	protected Object getParams() {
		return this.data;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
}
