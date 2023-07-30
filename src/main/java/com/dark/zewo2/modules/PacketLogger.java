/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package com.dark.zewo2.modules;

import com.dark.zewo2.Addon;
import meteordevelopment.meteorclient.settings.PacketListSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.network.PacketUtils;
import net.minecraft.network.packet.Packet;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.packet.s2c.login.*;
import net.minecraft.network.packet.s2c.play.*;
import net.minecraft.network.packet.s2c.query.QueryPongS2CPacket;
import net.minecraft.network.packet.s2c.query.QueryResponseS2CPacket;


import java.util.Arrays;
import java.util.Set;

public class PacketLogger extends Module{
    public PacketLogger() {
        super(Addon.CATEGORY, "Packet-Logger", "Tells you incoming packet info.");
    }

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Set<Class<? extends Packet<?>>>> s2cPackets = sgGeneral.add(new PacketListSetting.Builder()
        .name("S2C-packets")
        .description("Server-to-client packets to log.")
        .filter(aClass -> PacketUtils.getS2CPackets().contains(aClass))
        .build()
    );

    @EventHandler
    private void onpacket(PacketEvent.Receive event){
        if (s2cPackets.get().contains(event.packet)) info(getresponse(event.packet));
    }

    private String getresponse(Packet<?> packet) {

        if (packet instanceof LoginCompressionS2CPacket p) return "LoginCompression compressionThreshold:" + p.getCompressionThreshold();
        if (packet instanceof LoginDisconnectS2CPacket p) return "LoginDisconnect reason:" + p.getReason();
        if (packet instanceof LoginHelloS2CPacket p) return "LoginHello serverId:" + p.getServerId();
        if (packet instanceof LoginQueryRequestS2CPacket p) return "LoginQueryRequest id:" + p.getQueryId() + " channel:" + p.getChannel().toString();
        if (packet instanceof LoginSuccessS2CPacket p) return "LoginSuccess name:" + p.getProfile().getName() + " UUID:" + p.getProfile().getId();

        if (packet instanceof AdvancementUpdateS2CPacket p) return "AdvancementUpdate shouldClear:" + p.shouldClearCurrent();
        if (packet instanceof BlockBreakingProgressS2CPacket p) return "BlockBreakingProgress x:" + p.getPos().getX() + " y:" + p.getPos().getY() + " z:" + p.getPos().getZ() + " entityId:" + p.getEntityId() + " progress:" + p.getProgress();
        if (packet instanceof BlockEntityUpdateS2CPacket p) return "BlockEntityUpdate type:" + p.getBlockEntityType().toString() + " x:" + p.getPos().getX() + " y:" + p.getPos().getY() + " z:" + p.getPos().getZ();
        if (packet instanceof BlockEventS2CPacket p) return "BlockEvent x:" + p.getPos().getX() + " y:" + p.getPos().getY() + " z:" + p.getPos().getZ() + " block:" + p.getBlock().getName() + " type:" + p.getType() + " data:" + p.getData();
        if (packet instanceof BlockUpdateS2CPacket p) return "BlockUpdate x:" + p.getPos().getX() + " y:" + p.getPos().getY() + " z:" + p.getPos().getZ() + " block:" + p.getState().getBlock();
        if (packet instanceof BossBarS2CPacket) return "BossBar";
        if (packet instanceof BundleS2CPacket) return "PacketBundle";
        if (packet instanceof ChatMessageS2CPacket p) return "ChatMessage message:" + p.unsignedContent() + " sender:" + p.sender().toString() + " index:" + p.index();
        if (packet instanceof ChatSuggestionsS2CPacket p) return "ChatSuggestions action:" + p.action().name() + " entries: \n" + p.entries().toString();
        if (packet instanceof ChunkBiomeDataS2CPacket p) return "ChunkBiomeData size:" + p.chunkBiomeData().size();
        if (packet instanceof ChunkDataS2CPacket p) return "ChunkData x:" + p.getX() + " z:" + p.getZ();
        if (packet instanceof ChunkDeltaUpdateS2CPacket) return "ChunkDeltaUpdate";
        if (packet instanceof ChunkLoadDistanceS2CPacket p) return "ChunkLoadDistance distance:" + p.getDistance();
        if (packet instanceof ChunkRenderDistanceCenterS2CPacket p) return "ChunkRenderDistanceCenter x:" + p.getChunkX() + " z:" + p.getChunkZ();
        if (packet instanceof ClearTitleS2CPacket p) return "ClearTitle shouldReset:" + p.shouldReset();
        if (packet instanceof CloseScreenS2CPacket p) return "CloseScreen syncId" + p.getSyncId();
        if (packet instanceof CommandSuggestionsS2CPacket p) return "CommandSuggestions completionId:" + p.getCompletionId() + " suggestions: \n" + (p.getSuggestions().getList().toString());
        if (packet instanceof CommandTreeS2CPacket) return "CommandTree";
        if (packet instanceof CooldownUpdateS2CPacket p) return "CooldownUpdate item:" + p.getItem() + " cooldown:" + p.getCooldown();
        if (packet instanceof CraftFailedResponseS2CPacket p) return "CraftFailedResponse syncId:" + p.getSyncId() + " recipeId:" + p.getRecipeId().toString();
        if (packet instanceof CustomPayloadS2CPacket p) return "CustomPayload channel:" + p.getChannel();
        if (packet instanceof DamageTiltS2CPacket p) return "DamageTilt yaw:" + p.yaw() + " id:" + p.id();
        if (packet instanceof DeathMessageS2CPacket p) return "DeathMessage entityId:" + p.getEntityId() + " message:" + p.getMessage().getString();
        if (packet instanceof DifficultyS2CPacket p) return "Difficulty difficulty:" + p.getDifficulty().getName();
        if (packet instanceof DisconnectS2CPacket p) return "Disconnect reason:" + p.getReason().getString();
        if (packet instanceof EndCombatS2CPacket) return "EndCombat";
        if (packet instanceof EnterCombatS2CPacket) return "EnterCombat";
        if (packet instanceof EntitiesDestroyS2CPacket p) return "EntitiesDestroy size:" + p.getEntityIds().size();
        if (packet instanceof EntityAnimationS2CPacket p) return "EntityAnimation id:" + p.getId() + " animationId:" + p.getAnimationId();
        if (packet instanceof EntityAttachS2CPacket p) return "EntityAttack attackedId:" + p.getAttachedEntityId() + " holdingId:" + p.getHoldingEntityId();
        if (packet instanceof EntityAttributesS2CPacket p) return "EntityAttributes entityId:" + p.getEntityId();
        if (packet instanceof EntityDamageS2CPacket p) return "EntityDamage entityId:" + p.entityId() + " causeId:" + p.sourceCauseId() + " directId:" + p.sourceDirectId() + " sourceType:" + p.sourceTypeId();
        if (packet instanceof EntityEquipmentUpdateS2CPacket p) return "EntityEquipment entityId:" + p.getId();
        if (packet instanceof EntityPassengersSetS2CPacket p) return "EntityPassengersSet entityId:" + p.getId() + Arrays.toString(p.getPassengerIds());
        if (packet instanceof EntityPositionS2CPacket p) return "EntityPosition id:" + p.getId() + " x:" + p.getX() + " y:" + p.getY() + " z:" + p.getZ() + " yaw:" + p.getYaw() + " pitch:" + p.getPitch() + " ground:" + p.isOnGround();
        if (packet instanceof EntityS2CPacket p) return "Entity id:" + p.getEntity(mc.world).getId() + " deltaX:" + p.getDeltaX() + " deltaY:" + p.getDeltaY() + " deltaZ:" + p.getDeltaZ() + " yaw:" + p.getYaw() + " pitch:" + p.getPitch() + " hasRotation:" + p.hasRotation() + " posChange:" + p.isPositionChanged() + " ground:" + p.isOnGround();
        if (packet instanceof EntitySetHeadYawS2CPacket p) return "EntitySetHeadYaw id:" + p.getEntity(mc.world).getId();
        if (packet instanceof EntitySpawnS2CPacket p) return "EntitySpawn id:" + p.getId() + " type:" + p.getEntityType() + " x:" + p.getX() + " y:" + p.getY() + " z:" + p.getZ() + " yaw:" + p.getYaw() + " pitch:" + p.getPitch() + " velX:" + p.getVelocityX() + " velY:" + p.getVelocityY() + " velZ:" + p.getVelocityZ() + " headYaw:" + p.getHeadYaw() + " UUID:" + p.getUuid() + " data:" + p.getEntityData();
        if (packet instanceof EntityStatusEffectS2CPacket p) return "EntityStatusEffect entityId:" + p.getEntityId() + " effect:" + p.getEffectId().getName() + " amplifier:" + p.getAmplifier() + " duration:" + p.getDuration() + " showIcon:" + p.shouldShowIcon() + " particles:" + p.shouldShowParticles() + " ambient:" + p.isAmbient();
        if (packet instanceof EntityStatusS2CPacket p) return "EntityStatus status:" + p.getStatus() + " id:" + p.getEntity(mc.world).getId();
        if (packet instanceof EntityTrackerUpdateS2CPacket p) return "EntityTrackerUpdate id:" + p.id() ;
        if (packet instanceof EntityVelocityUpdateS2CPacket p) return "EntityVelocityUpdate id:" + p.getId() + " x:" + p.getVelocityX() + " y:" + p.getVelocityY() + " z:" + p.getVelocityZ();
        if (packet instanceof ExperienceBarUpdateS2CPacket p) return "ExperienceBarUpdate exp:" + p.getExperience() + " level" + p.getExperienceLevel() + " barProgress:" + p.getBarProgress();
        if (packet instanceof ExperienceOrbSpawnS2CPacket p) return "ExperienceOrbSpawn id:" + p.getId() + " x:" + p.getX() + " y:" + p.getY() + " z:" + p.getZ() + " expAmount" + p.getExperience();
        if (packet instanceof ExplosionS2CPacket p) return "Explosion x:" + p.getX() + " y:" + p.getY() + " z:" + p.getZ() + " radius:" + p.getRadius() + " playerVelX:" + p.getPlayerVelocityX() + " playerVelY:" + p.getPlayerVelocityY() + " playerVelZ:" + p.getPlayerVelocityZ();
        if (packet instanceof FeaturesS2CPacket) return "Features";
        if (packet instanceof GameJoinS2CPacket p) return "GameJoin debugWorld:" + p.debugWorld() + " flatWorld:" + p.flatWorld() + " gameMode:" + p.gameMode().getName() + " viewDist:"  + p.viewDistance() + " simulationDist:" + p.simulationDistance() + " dimensionId:" + p.dimensionId().getValue().toString() + " hardcode:" + p.hardcore() + " ownId:" + p.playerEntityId() + " maxPlayers:" + p.maxPlayers() + " prevGameMode:" + p.previousGameMode().getName() + " showDeathScreen:" + p.showDeathScreen() + " reducedDebugInfo:" + p.reducedDebugInfo();
        if (packet instanceof GameMessageS2CPacket p) return "GameMessage content:" + p.content().getString();
        if (packet instanceof GameStateChangeS2CPacket p) return "GameStateChange val:" + p.getValue() + " reason:" + p.getReason().toString();
        if (packet instanceof HealthUpdateS2CPacket p) return "HealthUpdate health:" + p.getHealth() + " food:" + p.getFood() + " saturation:" + p.getSaturation();
        if (packet instanceof ItemPickupAnimationS2CPacket p) return "ItemPickupAnimation id:" + p.getEntityId() + " collectorEntity:" + p.getCollectorEntityId() + " stackAmount:" + p.getStackAmount();
        if (packet instanceof KeepAliveS2CPacket p) return "KeepAlive id:" + p.getId();
        if (packet instanceof LightUpdateS2CPacket p) return "LightUpdate chunkX:" + p.getChunkX() + " chunkZ:" + p.getChunkZ();
        if (packet instanceof LookAtS2CPacket) return "LookAt";
        if (packet instanceof MapUpdateS2CPacket p) return "MapUpdate id:" + p.getId() + " scale:" + p.getScale() + " locked:" + p.isLocked();
        if (packet instanceof NbtQueryResponseS2CPacket p) return "NbtQueryResponse transactionId:" + p.getTransactionId();
        if (packet instanceof OpenHorseScreenS2CPacket p) return "OpenHorseScreen horseId:" + p.getHorseId() + " syncId:" + p.getSyncId() + " slotCount:" + p.getSlotCount();
        if (packet instanceof OpenScreenS2CPacket p) return "OpenScreen name:" + p.getName().getString() + " syncId:" + p.getSyncId();
        if (packet instanceof OpenWrittenBookS2CPacket p) return "OpenWrittenBook hand:" + p.getHand().name();
        if (packet instanceof OverlayMessageS2CPacket p) return "OverlayMessage message:" + p.getMessage().getString();
        if (packet instanceof ParticleS2CPacket p) return "Particle x:" + p.getX() + " y:" + p.getY() + " z:" + p.getZ() + " count:" + p.getCount() + " offsetX:" + p.getOffsetX() + " offsetY:" + p.getOffsetY() + " offsetZ:" + p.getOffsetZ() + " speed:" + p.getSpeed() + " parameters:" + p.getParameters().asString() + " longDist:" + p.isLongDistance();
        if (packet instanceof PlayerAbilitiesS2CPacket p) return "PlayerAbilities isFlying:" + p.isFlying() + " allowFlying:" + p.allowFlying() + " flySpeed:" + p.getFlySpeed() + " walkSpeed:" + p.getWalkSpeed() + " creative:" + p.isCreativeMode() + " invulnerable:" + p.isInvulnerable();
        if (packet instanceof PlayerActionResponseS2CPacket p) return "PlayerActionResponse sequence:" + p.sequence();
        if (packet instanceof PlayerListHeaderS2CPacket p) return "PlayerListHeader header:" + p.getHeader().getString() + " footer:" + p.getFooter().getString();
        if (packet instanceof PlayerPositionLookS2CPacket p) return "PlayerPositionLook x:" + p.getX() + " y:" + p.getY() + " z:" + p.getZ() + " yaw:" + p.getYaw() + " pitch:" + p.getPitch() + " id:" + p.getTeleportId();
        if (packet instanceof PlayerRemoveS2CPacket p) return "PlayerRemove UUIDs: \n" + (p.profileIds().toString());
        if (packet instanceof PlayerRespawnS2CPacket p) return "PlayerRespawn gameMode:" + p.getGameMode().getName() + " prevGameMode:" + p.getPreviousGameMode().getName() + (!p.getLastDeathPos().isPresent() ? " <deathPosNotPresent>" : " deathX:" + p.getLastDeathPos().get().getPos().getX() + " deathY:" + p.getLastDeathPos().get().getPos().getY() + " deathZ:" + p.getLastDeathPos().get().getPos().getZ()) + " debugWorld:" + p.isDebugWorld() + " flatWorld:" + p.isFlatWorld();
        if (packet instanceof PlayerSpawnPositionS2CPacket p) return "PlayerSpawnPosition x:" + p.getPos().getX() + " y:" + p.getPos().getY() + " z:" + p.getPos().getZ() + " angle:" + p.getAngle();
        if (packet instanceof PlayerSpawnS2CPacket p) return "PlayerSpawn id:" + p.getId() + " x:" + p.getX() + " y:" + p.getY() + " z:" + p.getZ() + " yaw:" + p.getYaw() + " pitch:" + p.getPitch() + " UUID:" + p.getPlayerUuid();
        if (packet instanceof PlayPingS2CPacket p) return "PlayPing parameter:" + p.getParameter();
        if (packet instanceof PlaySoundFromEntityS2CPacket p) return "PlaySoundFromEntity entityId:" + p.getEntityId() + " category:" + p.getCategory().getName() + " sound:" + p.getSound().getType().name() + " pitch:" + p.getPitch() + " volume:" + p.getVolume() + " seed:" + p.getSeed();
        if (packet instanceof PlaySoundS2CPacket p) return "PlaySound x:" + p.getX() + " y:" + p.getY() + " z:" + p.getZ() + " category:" + p.getCategory() + " sound:" + p.getSound().getType().name() + " pitch:" + p.getPitch() + " volume:" + p.getVolume() + " seed:" + p.getSeed();
        if (packet instanceof ProfilelessChatMessageS2CPacket p) return "ProfilelessChatMessage message:" + p.message();
        if (packet instanceof RemoveEntityStatusEffectS2CPacket p) return "RemoveEntityStatusEffect effect:" + (p.getEffectType() == null ? "null" : p.getEffectType().getName());
        if (packet instanceof RemoveMessageS2CPacket p) return "RemoveMessage signature:" + p.messageSignature().toString();
        if (packet instanceof ResourcePackSendS2CPacket p) return "ResourcePackSend URL:" + p.getURL() + " SHA1:" + p.getSHA1() + " prompt:" + p.getPrompt() + " required:" + p.isRequired();
        if (packet instanceof ScoreboardDisplayS2CPacket p) return "ScoreboardDisplay name:" + p.getName() + " slot:" + p.getSlot();
        if (packet instanceof ScoreboardObjectiveUpdateS2CPacket p) return "ScoreboardObjectiveUpdate name:" + p.getName() + " type:" + p.getType() + " mode:" + p.getMode() + " displayName:" + p.getDisplayName();
        if (packet instanceof ScoreboardPlayerUpdateS2CPacket p) return "ScoreboardPlayerUpdate playerName:" + p.getPlayerName() + " objectiveName:" + p.getObjectiveName() + " updateMode:" + p.getUpdateMode() + " score:" + p.getScore();
        if (packet instanceof ScreenHandlerPropertyUpdateS2CPacket p) return "ScreenHandlerPropertyUpdate syncId:" + p.getSyncId() + " propertyId:" + p.getPropertyId() + " value:" + p.getValue();
        if (packet instanceof ScreenHandlerSlotUpdateS2CPacket p) return "ScreenHandlerSlotUpdate syncId:" + p.getSyncId() + " slot:" + p.getSlot() + " item:" + p.getItemStack().getItem().getName() + " count:" + p.getItemStack().getCount() + " revision:" + p.getRevision();
        if (packet instanceof SelectAdvancementTabS2CPacket p) return "SelectAdvancementTab tabId:" + p.getTabId();
        if (packet instanceof ServerMetadataS2CPacket p) return "ServerMetadata description:" + p.getDescription() + " secureChat:" + p.isSecureChatEnforced();
        if (packet instanceof SetCameraEntityS2CPacket p) return "SetCameraEntity id:" + p.getEntity(mc.world).getId();
        if (packet instanceof SetTradeOffersS2CPacket p) return "SetTradeOffers syncId:" + p.getSyncId() + " experience:" + p.getExperience() + " levelProgress:" + p.getLevelProgress() + " leveled:" + p.isLeveled() + " isRefreshable:" + p.isRefreshable() + " offers: \n" + (p.getOffers().toString());
        if (packet instanceof SignEditorOpenS2CPacket p) return "SignEditorOpen x:" + p.getPos().getX() + " y:" + p.getPos().getY() + " z:" + p.getPos().getZ();
        if (packet instanceof SimulationDistanceS2CPacket p) return "SimulationDistance distance:" + p.simulationDistance();
        if (packet instanceof StopSoundS2CPacket p) return "StopSound soundId:" + p.getSoundId() + " category:" + (p.getCategory() == null ? "null" : p.getCategory().getName());
        if (packet instanceof SubtitleS2CPacket p) return "Subtitle subtitle:" + p.getSubtitle().getString();
        if (packet instanceof SynchronizeTagsS2CPacket) return "SynchronizeTags";
        if (packet instanceof TeamS2CPacket p) return "Team teamName:" + p.getTeamName() + " names: \n" + (p.getPlayerNames().toString()) + "";
        if (packet instanceof TitleFadeS2CPacket p) return "TitleFade fadeInTicks:" + p.getFadeInTicks() + " stayTicks:" + p.getStayTicks() + " fadeOutTicks:" + p.getFadeOutTicks();
        if (packet instanceof UnloadChunkS2CPacket p) return "UnloadChunk x:" + p.getX() + " z:" + p.getZ();
        if (packet instanceof UnlockRecipesS2CPacket p) return "UnlockRecipes action:" + p.getAction().name();
        if (packet instanceof UpdateSelectedSlotS2CPacket p) return "UpdateSelectedSlot slot:" + p.getSlot();
        if (packet instanceof VehicleMoveS2CPacket p) return "VehicleMove x:" + p.getX() + " y:" + p.getY() + " z:" + p.getZ() + " yaw:" + p.getYaw() + " pitch:" + p.getPitch();
        if (packet instanceof WorldBorderCenterChangedS2CPacket p) return "WorldBorderCenterChanged x:" + p.getCenterX() + " z:" + p.getCenterZ();
        if (packet instanceof WorldBorderInitializeS2CPacket p) return "WorldBorderInitialize centerX:" + p.getCenterX() + " centerZ:" + p.getCenterZ() + " size:" + p.getSize() + " sizeLerpTarget:" + p.getSizeLerpTarget() + " sizeLerpTime:" + p.getSizeLerpTime() + " warningBlocks:" + p.getWarningBlocks() + " warningTime:" + p.getWarningTime() + " maxRadius:" + p.getMaxRadius();
        if (packet instanceof WorldBorderInterpolateSizeS2CPacket p) return "WorldBorderInterpolateSize size:" + p.getSize() + " sizeLerpTarget:" + p.getSizeLerpTarget() + " sizeLerpTime:" + p.getSizeLerpTime();
        if (packet instanceof WorldBorderSizeChangedS2CPacket p) return "WorldBorderSizeChanged sizeLerpTarget:" + p.getSizeLerpTarget();
        if (packet instanceof WorldBorderWarningBlocksChangedS2CPacket p) return "WorldBorderWarningBlocksChanged warnignBlocks:" + p.getWarningBlocks();
        if (packet instanceof WorldBorderWarningTimeChangedS2CPacket p) return "WorldBorderWarningTimeChanged time:" + p.getWarningTime();
        if (packet instanceof WorldEventS2CPacket p) return "WorldEvent x:" + p.getPos().getX() + " y:" + p.getPos().getY() + " z:" + p.getPos().getZ() + " eventId:" + p.getEventId() + " data:" + p.getData() + " global:" + p.isGlobal();
        if (packet instanceof WorldTimeUpdateS2CPacket p)  return "WorldTimeUpdate time:" + p.getTime() + " timeOfDay:" + p.getTimeOfDay();

        if (packet instanceof QueryPongS2CPacket p) return "QueryPong startTime:" + p.getStartTime();
        if (packet instanceof QueryResponseS2CPacket p) return "QueryResponse description:" + p.metadata().description() + " onlinePlayers" + (p.metadata().players().isPresent() ? p.metadata().players().get().online() : "null") + " maxPlayers" + (p.metadata().players().isPresent() ? p.metadata().players().get().max() : "null");

        return null;
    }
}

