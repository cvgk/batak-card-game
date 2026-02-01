package batakprogramlama;
import java.util.*;
public class Kart implements Comparable<Kart>{
    private Tip tip;
    private Deger deger;

    public Kart(Tip tip,Deger deger){
        this.tip = tip;
        this.deger = deger;
    }

    public Tip getTip() {
        return tip;
    }

    public Deger getDeger() {
        return deger;
    }
    @Override
    public int compareTo(Kart kart){
        if((this.tip.getValue()==kart.tip.getValue()))
            return this.deger.getValue()-kart.deger.getValue();
        return this.tip.getValue()-kart.tip.getValue();

    }
    // Kart.java İÇİNDE OLMALI:
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Kart kart = (Kart) o;
        return tip == kart.tip && deger == kart.deger; // Tip ve Deger enum'ları olduğunu varsayıyoruz
    }

    @Override
    public int hashCode() {
        return Objects.hash(tip, deger); // Tip ve Deger enum'ları olduğunu varsayıyoruz
        // (java.util.Objects import edilmeli)
    }
    public String toString(){
        return tip.toString() + " " + deger.toString();
    }
}