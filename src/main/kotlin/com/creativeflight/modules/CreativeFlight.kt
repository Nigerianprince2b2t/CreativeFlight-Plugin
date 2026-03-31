package com.creativeflight.modules

import com.lambda.event.events.TickEvent
import com.lambda.event.listener.SafeListener.Companion.listen
import com.lambda.module.Module
import com.lambda.module.tag.ModuleTag


object CreativeFlight : Module(
    name = "CreativeFlight",
    description = "Allows you to fly in survival mode.",
    tag = ModuleTag.MOVEMENT,
) {
    private val speed by setting("Speed", 0.1f, 0.0f..2.0f, 0.01f)

    init {
        listen<TickEvent.Post> {
            player.abilities.flying = true
            player.abilities.allowFlying = true
            player.abilities.setFlySpeed(speed)
        }

        onDisable {
            if (player.abilities.creativeMode || player.isSpectator) return@onDisable
            player.abilities.flying = false
            player.abilities.allowFlying = false
            player.abilities.setFlySpeed(0.05f)
        }
    }
}
