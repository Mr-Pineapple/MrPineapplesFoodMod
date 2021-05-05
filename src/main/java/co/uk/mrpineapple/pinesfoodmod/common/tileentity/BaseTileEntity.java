package co.uk.mrpineapple.pinesfoodmod.common.tileentity;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.stream.Stream;

public class BaseTileEntity extends TileEntity {
    public BaseTileEntity(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
    }

    protected void syncToClient() {
        this.setChanged();
        if(this.level != null && !this.level.isClientSide) {
            if(this.level instanceof ServerWorld) {
                SUpdateTileEntityPacket packet = this.getUpdatePacket();
                if(packet != null) {
                    ServerWorld serverWorld = (ServerWorld)this.level;
                    Stream<ServerPlayerEntity> playerEntityStream = serverWorld.getChunkSource().chunkMap.getPlayers(new ChunkPos(this.worldPosition), false);
                    playerEntityStream.forEach(player -> player.connection.send(packet));
                }
            }
        }
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return this.save(new CompoundNBT());
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(getBlockPos(), 0, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        this.load(this.getBlockState(), pkt.getTag());
    }
}
