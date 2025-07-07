package miyucomics.hexodus

import dev.onyxstudios.cca.api.v3.component.ComponentKey
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry
import dev.onyxstudios.cca.api.v3.component.ComponentV3
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer
import gravity_changer.GravityComponent
import net.minecraft.entity.Entity
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.math.Direction
import kotlin.jvm.java

class HexodusComponents : EntityComponentInitializer {
	override fun registerEntityComponentFactories(registry: EntityComponentFactoryRegistry) {
		registry.registerFor(Entity::class.java, HEXODUS) { HexodusComponent(Direction.DOWN, 1.0) }
	}

	companion object {
		val HEXODUS: ComponentKey<HexodusComponent> = ComponentRegistry.getOrCreate(HexodusMain.id("hexodus"), HexodusComponent::class.java)
	}
}

class HexodusComponent(var direction: Direction, var strength: Double) : ComponentV3, AutoSyncedComponent, CommonTickingComponent {
	fun setAlteredGravity(entity: Entity, newDirection: Direction, newStrength: Double) {
		direction = newDirection
		strength = newStrength
		HexodusComponents.HEXODUS.sync(entity)
	}

	fun alterGravity(component: GravityComponent) {
		component.applyGravityDirectionEffect(direction, null, 200.0)
		component.applyGravityStrengthEffect(strength)
	}

	override fun writeToNbt(compound: NbtCompound) {
		compound.putInt("x", direction.offsetX)
		compound.putInt("y", direction.offsetY)
		compound.putInt("z", direction.offsetZ)
		compound.putDouble("strength", strength)
	}

	override fun readFromNbt(compound: NbtCompound) {
		direction = Direction.fromVector(compound.getInt("x"), compound.getInt("y"), compound.getInt("z")) ?: Direction.DOWN
		strength = compound.getDouble("strength")
	}

	override fun tick() {
	}
}
