package DeepSpiringMod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.ActionType;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import com.megacrit.cardcrawl.vfx.combat.CleaveEffect;
import com.megacrit.cardcrawl.vfx.combat.WhirlwindEffect;

public class DeepfectStrikeAction extends AbstractGameAction {
   public int[] multiDamage;
   private boolean freeToPlayOnce = false;
   private DamageInfo.DamageType damageType;
   private AbstractPlayer p;
   private int selfDamage;
   private int energyOnUse = -1;

   public DeepfectStrikeAction(AbstractPlayer p, int[] multiDamage, int selfDamage, DamageInfo.DamageType damageType, boolean freeToPlayOnce, int energyOnUse) {
      this.multiDamage = multiDamage;
      this.damageType = damageType;
      this.p = p;
      this.selfDamage = selfDamage;
      this.freeToPlayOnce = freeToPlayOnce;
      this.duration = Settings.ACTION_DUR_XFAST;
      this.actionType = ActionType.SPECIAL;
      this.energyOnUse = energyOnUse;
   }

   public void update() {
      int effect = EnergyPanel.totalCount;
      if (this.energyOnUse != -1) {
         effect = this.energyOnUse;
      }

      if (this.p.hasRelic("Chemical X")) {
         effect += 2;
         this.p.getRelic("Chemical X").flash();
      }

      if (effect > 0) {
         for(int i = 0; i < effect; ++i) {
            if (i == 0) {
               this.addToBot(new SFXAction("ATTACK_WHIRLWIND"));
               this.addToBot(new VFXAction(new WhirlwindEffect(), 0.0F));
            }

            this.addToBot(new SFXAction("ATTACK_HEAVY"));
            this.addToBot(new VFXAction(this.p, new CleaveEffect(), 0.0F));
            this.addToBot(new DamageAllEnemiesAction(this.p, this.multiDamage, this.damageType, AttackEffect.NONE, true));
            this.addToBot(new DamageAction(this.p, new DamageInfo(this.p, this.selfDamage, DamageType.NORMAL), AttackEffect.BLUNT_LIGHT));
         }

         if (!this.freeToPlayOnce) {
            this.p.energy.use(EnergyPanel.totalCount);
         }
      }

      this.isDone = true;
   }
}
