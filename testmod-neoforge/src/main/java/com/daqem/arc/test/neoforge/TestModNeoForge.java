package dev.architectury.test.forge;

import com.daqem.arc.test.TestMod;
import net.neoforged.fml.common.Mod;

@Mod(TestMod.MOD_ID)
public class TestModNeoForge {
    public TestModForge() {
        TestMod.init();
    }
}
