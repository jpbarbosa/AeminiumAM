package examples.blogserver.PAM.normal.real;

public class PermissionResponse {

	public boolean accepted;
	
	public PutRequest req;
	
	public PermissionResponse(PutRequest req, boolean accepted){
		this.accepted = accepted;
		this.req = req;
	}
	
}
