package my.socket.mysql;

public class Name_Id{
	public String[] name = null;
	public int[] id = null;
	public Name_Id(int length) {
		this.name = new String[length];
		this.id = new int[length];
	}
}

