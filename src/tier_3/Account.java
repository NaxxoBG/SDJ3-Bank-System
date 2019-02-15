package tier_3;

import java.io.Serializable;

public class Account implements Serializable
{
   private static final long serialVersionUID = 1L;
   public String name;
   public int accNo = (int) 0;
   public double amount = (double) 0;

   public Account(int accNo, double amount, String name) {
      this.name = name;
      this.accNo = accNo;
      this.amount = amount;
   }

   public void setAmount(double amount) {
      this.amount = amount;
   }
   
   public void addAmount(double insertionAmount) {
      this.amount += insertionAmount;
   }
   
   public void removeAmount(double removalAmount) {
      this.amount -= removalAmount;
   }
   
   @Override
   public String toString() {
      return "Account [accNo=" + accNo + ", amount=" + amount + " name=" + name + "]";
   }
}