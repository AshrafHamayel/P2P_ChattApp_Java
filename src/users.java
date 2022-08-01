public class users {

    private String name,Dnmae;
    private int port;

    public users() {
        name = Dnmae = "";
        port = 0;
    }

    public users(String name, String Dnmae, int port) {
        this.name = name;
        this.Dnmae = Dnmae;
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public String getDnmae() {
        return Dnmae;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString(){
        return this.name;
    }
}
