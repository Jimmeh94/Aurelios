package com.aurelios.game.user;

import com.aurelios.util.keys.Key;
import com.aurelios.util.particles.ParticleModifier;

public class PlayerKeys {

    /**
     * Economy
     */
    public Key<Boolean> ECO_REQUESTS_AUTO_DENY = new Key<>(false);
    public Key<Boolean> ECO_OTHERS_SEE_BALANCE = new Key<>(true);

    //=======================================================================

    /**
     * Combat
     */
    public Key<Boolean> COMBAT_SEE_DAMAGE_INDICATORS = new Key<>(false);

    //=======================================================================

    /**
     * Chat
     */
    //This is for muting all chat besides NPC dialogue while the dialogue is displaying
    public Key<Boolean> CHAT_MUTE_ON_DIALOGUE = new Key<>(false);

    /**
     * MISC
     */
    public Key<ParticleModifier> MISC_PARTICLE_MODIFIER = new Key<>(ParticleModifier.NORMAL);
    public Key<Boolean> MISC_SHOW_POLLS_IN_CHAT = new Key<>(true);
    public Key<Boolean> MISC_FORMAT_TIME = new Key<>(true);

}
