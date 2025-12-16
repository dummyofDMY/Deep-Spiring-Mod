package DeepSpiringMod.actions;

import java.util.Iterator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import DeepSpiringMod.cards.AttentionHead;
import DeepSpiringMod.powers.RecursionDepth;
import DeepSpiringMod.cards.Overflow;
import DeepSpiringMod.relics.TryBlock;

public class ForwardPropagationAction extends AbstractGameAction {
    private int stackAmount = 0;

    public ForwardPropagationAction(int stackAmount) {
        this.stackAmount = stackAmount;
    }

    @Override
    public void update() {
        if (AbstractDungeon.player.discardPile.isEmpty()) {
            this.isDone = true;
            return;
        } else {
            for (int i = 0; i < this.stackAmount; ++i) {
                // 检查递归深度
                Iterator power_it = AbstractDungeon.player.powers.iterator();
                AbstractPower power;
                int recursion_depth = 0;
                while (power_it.hasNext()) {
                    power = (AbstractPower)power_it.next();
                    if (power instanceof RecursionDepth) {
                        recursion_depth = power.amount;
                    }
                }

                // System.out.print("recursion_depth = " + recursion_depth + "\n");
                if (recursion_depth >= 10) {
                    this.isDone = true;
                    // System.out.print("overflow happended\n");
                    return;
                }

                Iterator var5 = AbstractDungeon.player.discardPile.group.iterator();

                int to_play_num = 0;
                AbstractCard c;
                Boolean has_activated = false;
                while(var5.hasNext()) {
                    c = (AbstractCard)var5.next();

                    // try {
                    //     Class<?> clazz = c.getClass();
                    //     java.lang.reflect.Field field = clazz.getField("ID");
                    //     String ID = (String) field.get(null); // null 因为访问静态字段
                    //     System.out.print("card in drop = " + ID + "\n");
                    // } catch (Exception e) {
                    //     e.printStackTrace();
                    // }
                    if (c instanceof AttentionHead) {
                        to_play_num += c.magicNumber;
                        continue;
                    }

                    if (to_play_num > 0) {
                        has_activated = true;
                        to_play_num--;
                        AbstractCard tmp = c.makeStatEquivalentCopy();
                        tmp.purgeOnUse = true;
                        tmp.energyOnUse = tmp.costForTurn;

                        AbstractDungeon.player.limbo.addToBottom(tmp);
                        tmp.current_x = c.current_x;
                        tmp.current_y = c.current_y;
                        tmp.target_x = Settings.WIDTH / 2.0F * Settings.scale;
                        tmp.target_y = Settings.HEIGHT / 2.0F;

                        // try {
                        //     Class<?> clazz = c.getClass();
                        //     java.lang.reflect.Field field = clazz.getField("ID");
                        //     String ID = (String) field.get(null); // null 因为访问静态字段
                        //     System.out.print("card to use = " + ID + "\n");
                        // } catch (Exception e) {
                        //     e.printStackTrace();
                        // }

                        this.addToTop(new NewQueueCardAction(tmp, true, false, true));
                    }
                }
                if (has_activated) {
                    this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new RecursionDepth(AbstractDungeon.player, 1), 1));
                    if (recursion_depth >= 9) {
                        Boolean has_tryblock = false;
                        Iterator relic_it = AbstractDungeon.player.relics.iterator();
                        AbstractRelic relic;
                        while (relic_it.hasNext()) {
                            relic = (AbstractRelic)relic_it.next();
                            // try {
                            //     Class<?> clazz = relic.getClass();
                            //     java.lang.reflect.Field field = clazz.getField("ID");
                            //     String ID = (String) field.get(null); // null 因为访问静态字段
                            //     System.out.print("now relic = " + ID + "\n");
                            // } catch (Exception e) {
                            //     e.printStackTrace();
                            // }
                            if (relic instanceof TryBlock) {
                                has_tryblock = true;
                                relic.flash();
                            }
                        }
                        AbstractCard overflow_card = new Overflow();
                        if (has_tryblock) {
                            overflow_card.upgrade();
                        }
                        this.addToBot(new MakeTempCardInHandAction(overflow_card, 1));
                    }
                }
                this.isDone = true;
                return;
            }
            
        }
    }

}
