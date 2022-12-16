package me.redtea.serverbanksystem.messages.triumphgui

import dev.triumphteam.gui.builder.item.ItemBuilder
import me.redtea.carcadecomponents.ComponentWrapper

fun ItemBuilder.name(component: ComponentWrapper) = this.name(component.toComponent(false))

fun ItemBuilder.lore(component: ComponentWrapper) = this.lore(component.toComponentList())