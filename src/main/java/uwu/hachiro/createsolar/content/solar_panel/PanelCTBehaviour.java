package uwu.hachiro.createsolar.content.solar_panel;

import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.block.connected.ConnectedTextureBehaviour;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class PanelCTBehaviour extends ConnectedTextureBehaviour.Base {
    private final CTSpriteShiftEntry top;
    @Nullable private final CTSpriteShiftEntry bottom;

    public PanelCTBehaviour(CTSpriteShiftEntry top, @Nullable CTSpriteShiftEntry bottom) {
        this.top = top;
        this.bottom = bottom;
    }

    public PanelCTBehaviour(CTSpriteShiftEntry top) {
        this(top, null);
    }

    @Override
    public boolean connectsTo(BlockState state, BlockState other, BlockAndTintGetter reader, BlockPos pos, BlockPos otherPos, Direction face) {
        return state.getBlock().equals(other.getBlock());
    }

    @Override
    public @Nullable CTSpriteShiftEntry getShift(BlockState state, Direction direction, TextureAtlasSprite sprite) {
        return switch (direction) {
            case UP -> top;
            case DOWN -> bottom;
            default -> null;
        };
    }
}
