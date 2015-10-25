package lin.client.tcp;

@Command(commaand = 0)
public class DetectPackage extends CommandPackage {
	// static DetectPackage()
	// {
	// PackageManager.RegisterPackage(1, (byte[] bs)=>{
	// return new DetectPackage();
	// });
	// return;
	// }
	@Override
	public void parser(byte[] bs) {
		// return new DetectPackage();
	}

	public DetectPackage()// :base(1)
	{
	}

}
