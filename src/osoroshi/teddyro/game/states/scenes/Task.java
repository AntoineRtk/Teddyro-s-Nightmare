
package osoroshi.teddyro.game.states.scenes;

public class Task {
    
    private String params;
    private int info;
    
    public Task(int info, String params) {
        this.info = info;
        this.params = params;
    }
    
    public int getInfo() {
        return info;
    }
    
    public String getParams() {
        return params;
    }
}