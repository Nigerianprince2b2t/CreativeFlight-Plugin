package com.creativeflight.modules

import com.lambda.event.events.TickEvent
import com.lambda.event.events.PacketEvent
import com.lambda.event.listener.SafeListener.Companion.listen
import com.lambda.module.Module
import com.lambda.module.tag.ModuleTag
import net.minecraft.network.packet.c2s.play.UpdatePlayerAbilitiesC2SPacket
import net.minecraft.network.packet.s2c.play.PlayerAbilitiesS2CPacket

object CreativeFlight : Module(
    name = "CreativeFlight",
    description = "Allows you to fly with custom speed.",
    tag = ModuleTag.MOVEMENT,
) {
    private val speed by setting("Speed", 0.1f, 0.0f..2.0f, 0.01f)
    private var wasFlying = false

    init {
        onEnable {
            if (player.isSpectator) return@onEnable
            wasFlying = player.abilities.flying
            player.abilities.flying = true
            if (!player.abilities.creativeMode) player.abilities.allowFlying = true
        }

        onDisable {
            if (player.isSpectator) return@onDisable
            player.abilities.flying = wasFlying
            player.abilities.setFlySpeed(0.05f)
            if (!player.abilities.creativeMode) player.abilities.allowFlying = false
            connection.sendPacket(UpdatePlayerAbilitiesC2SPacket(player.abilities))
        }

        listen<TickEvent.Post> {
            if (player.isSpectator) return@listen
            player.abilities.flying = true
            player.abilities.setFlySpeed(speed)
            if (!player.abilities.creativeMode) player.abilities.allowFlying = true
        }

        listen<PacketEvent.Receive.Pre> { event ->
            if (event.packet is PlayerAbilitiesS2CPacket) event.cancel()
        }
    }
}