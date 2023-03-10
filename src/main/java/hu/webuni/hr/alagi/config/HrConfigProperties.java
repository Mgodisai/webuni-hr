package hu.webuni.hr.alagi.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConfigurationProperties(prefix = "hr.raise")
public class HrConfigProperties {

   private Default def;
   private Smart smart;

   public Default getDef() {
      return def;
   }

   public void setDef(Default def) {
      this.def = def;
   }

   public Smart getSmart() {
      return smart;
   }

   public void setSmart(Smart smart) {
      this.smart = smart;
   }

   public static class Default {
      private int percent;

      public int getPercent() {
         return percent;
      }

      public void setPercent(int percent) {
         this.percent = percent;
      }
   }

   public static class Smart {
      private Map<Double, Integer> limits;

      public Map<Double, Integer> getLimits() {
         return limits;
      }

      public void setLimits(Map<Double, Integer> smartLimits) {
         this.limits = smartLimits;
      }
   }
}
