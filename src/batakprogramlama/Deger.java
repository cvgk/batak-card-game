package batakprogramlama;

public enum Deger {
    AS(14),PAPAZ(13),KIZ(12),
    VALE(11),ON(10),DOKUZ(9),SEKIZ(8),YEDI(7)
  ,ALTI(6),BES(5),DORT(4),UC(3),IKI(2);
    private int value;
    Deger(int value){
        this.value = value;
    }
    public int getValue(){
        return value;
    }
}
