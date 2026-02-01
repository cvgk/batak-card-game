package batakprogramlama;

public enum Tip {
    MACA(4),KUPA(3),KARO(2),SINEK(1);
    private int value;

    Tip(int value){
        this.value = value;
    }
    public int getValue(){
        return value;
    }
}
