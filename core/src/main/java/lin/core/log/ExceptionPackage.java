package lin.core.log;

import java.util.HashMap;
import java.util.Map;

import lin.comm.http.HttpMethod;
import lin.comm.http.HttpPackage;
import lin.comm.http.annotation.HttpPackageMethod;
import lin.comm.http.annotation.HttpPackageReturnType;

//@HttpPackageUrl("/exception/add.action")
@HttpPackageMethod(HttpMethod.POST)
@HttpPackageReturnType(String.class)
public class ExceptionPackage extends HttpPackage{

	private String uuid;
	private String info;
	private String deviceInfo;
	
	public ExceptionPackage(String url){
		super(url);
	}
	@Override
	public Map<String, Object> getParams() {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("uuid", uuid);
		map.put("info", info);
		map.put("deviceInfo", deviceInfo);
		return map;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getDeviceInfo() {
		return deviceInfo;
	}
	public void setDeviceInfo(String deviceInfo) {
		this.deviceInfo = deviceInfo;
	}
	
}

//@implementation ExceptionPackage
//
//-(id)init{
//    return [super initWithUrl:@"/exception/add.action" method:POST];
//}
//
//-(NSString *)uuid{
//    return [self[@"uuid"] asString:@""];
//}
//-(void)setUuid:(NSString *)uuid{
//    [self setValue:uuid forName:@"uuid"];
//}
//
//-(NSString *)info{
//    return [self[@"info"] asString:@""];
//}
//-(void)setInfo:(NSString *)info{
//    [self setValue:info forName:@"info"];
//}
//
//-(NSString *)deviceInfo{
//    return [self[@"deviceInfo"] asString:@""];
//}
//-(void)setDeviceInfo:(NSString *)deviceInfo{
//    [self setValue:deviceInfo forName:@"deviceInfo"];
//}
//
//@end