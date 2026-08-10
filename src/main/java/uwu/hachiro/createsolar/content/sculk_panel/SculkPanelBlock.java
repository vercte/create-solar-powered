package uwu.hachiro.createsolar.content.sculk_panel;

import com.simibubi.create.foundation.block.IBE;
import net.minecraft.world.level.block.entity.BlockEntityType;
import uwu.hachiro.createsolar.SolarBlockEntities;
import uwu.hachiro.createsolar.content.panel.BasePanelBlock;

public class SculkPanelBlock extends BasePanelBlock implements IBE<SculkPanelBlockEntity> {
    public SculkPanelBlock(Properties properties) {
        super(properties);
    }


    @Override
    public Class<SculkPanelBlockEntity> getBlockEntityClass() {
        return SculkPanelBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends SculkPanelBlockEntity> getBlockEntityType() {
        return SolarBlockEntities.SCULK_PANEL.get();
    }
}
