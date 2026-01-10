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

import DeepSpiringMod.cards.AttentionHead;
import DeepSpiringMod.powers.RecursionDepthPower;
import DeepSpiringMod.cards.Overflow;
import DeepSpiringMod.helpers.ModHelper;
import DeepSpiringMod.cards.FeatureMap;

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
                    if (power instanceof RecursionDepthPower) {
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
                int damage_sum = 0, block_sum = 0;

                // 检测是否有卷积
                Boolean has_conv = AbstractDungeon.player.hasPower(ModHelper.makePath("Convolution"));
                double conv_factor = 0;
                if (has_conv) {
                    conv_factor = AbstractDungeon.player.getPower(ModHelper.makePath("Convolution")).amount;
                    // conv_factor = (2 - Math.pow(0.5, conv_stack - 1)) / 0.5 - 1;
                    // conv_factor /= 2;
                }

                // 检测loss和overfitting的情况
                int AP_factor = 0, overfitting_factor = 0;
                if (AbstractDungeon.player.hasPower(ModHelper.makePath("AP"))) {
                    AP_factor = AbstractDungeon.player.getPower(ModHelper.makePath("AP")).amount;
                }
                if (AbstractDungeon.player.hasPower(ModHelper.makePath("Overfitting")) && !AbstractDungeon.player.hasPower(ModHelper.makePath("SOTA"))) {
                    overfitting_factor = AbstractDungeon.player.getPower(ModHelper.makePath("Overfitting")).amount;
                }
                int loss = Math.abs(AP_factor - overfitting_factor);
                loss = loss > 0 ? loss : 1;
                // double precision = (1 - loss_factor) * (0.25 - 0.05 * overfitting_factor);
                // precision = precision > 0 ? precision : 0;

                while(var5.hasNext()) {
                    c = (AbstractCard)var5.next();
                    
                    // 累积打防数值
                    System.out.print("damage = " + c.baseDamage + ", block = " + c.baseBlock + "\n");
                    if (has_conv) {
                        if (ModHelper.isMultipleHits(c)) {
                            damage_sum += c.baseDamage * c.baseMagicNumber > 0 ? c.baseDamage * c.baseMagicNumber : 0;
                        } else {
                            damage_sum += c.baseDamage > 0 ? c.baseDamage : 0;
                        }
                        block_sum += c.baseBlock > 0 ? c.baseBlock : 0;
                    }

                    // try {
                    //     Class<?> clazz = c.getClass();
                    //     java.lang.reflect.Field field = clazz.getField("ID");
                    //     String ID = (String) field.get(null); // null 因为访问静态字段
                    //     System.out.print("card in drop = " + ID + "\n");
                    // } catch (Exception e) {
                    //     e.printStackTrace();
                    // }
                    Boolean is_attention_head = false;
                    if (c instanceof AttentionHead) {
                        if (to_play_num > 0) {
                            to_play_num--;  // 下面进不了衰减的逻辑，这里补偿一下
                        }
                        to_play_num += c.magicNumber;
                        is_attention_head = true;
                    }

                    if (to_play_num > 0 && !is_attention_head) {
                        has_activated = true;
                        to_play_num--;
                        AbstractCard tmp = c.makeStatEquivalentCopy();
                        tmp.exhaust = true;
                        tmp.purgeOnUse = true;
                        tmp.energyOnUse = tmp.costForTurn;
                        tmp.freeToPlayOnce = true;
                        // if (tmp.magicNumber != -1) {
                        //     double magic_num = 2 * precision * tmp.magicNumber;
                        //     magic_num = Math.ceil(magic_num);
                        //     tmp.magicNumber = tmp.baseMagicNumber = (int)magic_num;
                        //     tmp.upgradedMagicNumber = true;
                        // }

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

                // 打出“特征图”
                if (damage_sum > 0 || block_sum > 0) {
                    System.out.print("damage_sum = " + damage_sum + ", block_sum = " + block_sum + "\n");
                    damage_sum = (int)Math.ceil(damage_sum * conv_factor * loss / 2);
                    block_sum = (int)Math.ceil(block_sum * conv_factor * loss / 2);
                    System.out.print("final damage_sum = " + damage_sum + ", block_sum = " + block_sum + "\n");
                    AbstractCard feature_map = new FeatureMap(damage_sum, block_sum);
                    feature_map.freeToPlayOnce = true;
                    this.addToTop(new NewQueueCardAction(feature_map, true, false, true));
                }

                // 处理“递归深度”的逻辑
                if (has_activated) {
                    this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new RecursionDepthPower(AbstractDungeon.player, 1), 1));
                    if (recursion_depth >= 9) {
                        Boolean has_tryblock = false;
                        if (AbstractDungeon.player.hasRelic(ModHelper.makePath("TryBlock"))) {
                            AbstractDungeon.player.getRelic(ModHelper.makePath("TryBlock")).flash();
                            has_tryblock = true;
                        }
                        // while (relic_it.hasNext()) {
                        //     relic = (AbstractRelic)relic_it.next();
                        //     // try {
                        //     //     Class<?> clazz = relic.getClass();
                        //     //     java.lang.reflect.Field field = clazz.getField("ID");
                        //     //     String ID = (String) field.get(null); // null 因为访问静态字段
                        //     //     System.out.print("now relic = " + ID + "\n");
                        //     // } catch (Exception e) {
                        //     //     e.printStackTrace();
                        //     // }
                        //     if (relic instanceof TryBlock) {
                        //         has_tryblock = true;
                        //         relic.flash();
                        //     }
                        // }
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
