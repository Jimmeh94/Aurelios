package com.aurelios.server.game.user.ai;

import com.aurelios.server.game.environment.nodes.Node;
import com.aurelios.server.game.user.ai.traits.*;
import com.aurelios.server.game.user.ai.traits.personality.Personality;
import com.aurelios.server.managers.Manager;
import com.aurelios.server.managers.Managers;
import com.aurelios.server.util.misc.StringUtils;
import org.spongepowered.api.text.Text;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class AIMetaData {

    private Node home;
    private UUID uuid;
    private String firstName, lastName;
    private int age;
    private Gender gender;
    private Personality personality;
    private Ambition ambition;
    private Role role;
    private Morality morality;
    private List<Aspiration> aspirations;
    private Race race;
    private EconomicClass economicClass;

    public AIMetaData(Node home, UUID uuid, String firstName, String lastName, int age, Gender gender,
                      Personality personality, Ambition ambition, Role role, Morality morality,
                       Race race, EconomicClass economicClass, List<Aspiration> aspirations) {
        this.home = home;
        this.uuid = uuid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.gender = gender;
        this.personality = personality;
        this.ambition = ambition;
        this.role = role;
        this.morality = morality;
        this.race = race;
        this.economicClass = economicClass;
        this.aspirations = aspirations;
    }

    public AIMetaData(Node home) {
        this.home = home;
        uuid = Managers.AI.getNextAvailableUUID();
        gender = Gender.getRandomGender();
        race = Race.getRandomRace();
        economicClass = EconomicClass.getRandomClass();

        Random random = new Random();
        age = random.nextInt(62) + 18; //age is between 18-80

        personality = Personality.getRandomPersonality();
        ambition = Ambition.getRandomAmibition();
        morality = Morality.getRandomMorality();
        role = Role.getRandomRole(this, home);

        aspirations = new CopyOnWriteArrayList<>();
        aspirations.addAll(Aspiration.getRandomAspiration(this));

        firstName = Names.getRandomFirstName(this);
        lastName = Names.getRandomLastName();
        // Find a better way to do this. Probably make sure there's no more than x amount of names per area in a node
        // Or just trust the randomness
        //while(!Managers.AI.isValidFirstName(firstName)){
          //  firstName = Names.getRandomFirstName(this);
        //}
    }

    public UUID getUuid() {
        return uuid;
    }

    public boolean equals(NPC obj) {
        return firstName.equalsIgnoreCase(obj.getMetaData().firstName) && lastName.equalsIgnoreCase(obj.getMetaData().lastName)
                && age == obj.getMetaData().age && gender == obj.getMetaData().gender;
    }

    /**
     * This is used to check if this has the prerequisites for getting a specific role and aspiration, and names
     * Not including Role in this list simply because we don't need it for the current checks this is used for
     * @return
     */
    public List<Enum> getAllEnums(){
        List<Enum> give = new ArrayList<>();
        give.add(gender);
        give.add(personality.getType());
        give.add(ambition);
        give.add(morality);
        give.add(race);
        give.add(economicClass);

        if(role != null){
            give.add(role);
        }
        if(aspirations != null){
            give.addAll(aspirations);
        }
        return give;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Personality getPersonality() {
        return personality;
    }

    public Ambition getAmbition() {
        return ambition;
    }

    public int getAge() {
        return age;
    }

    public Gender getGender() {
        return gender;
    }

    public Role getRole() {
        return role;
    }

    public Morality getMorality() {
        return morality;
    }

    public boolean hasAspiration(Aspiration aspiration){
        for(Aspiration a: aspirations){
            if(a == aspiration){
                return true;
            }
        }
        return false;
    }

    public List<Aspiration> getAspirations() {
        return aspirations;
    }

    public void print() {

        System.out.println(firstName + " " + lastName + ": ");
        System.out.println("- age: " + age);
        System.out.println("- gender: " + StringUtils.enumToString(gender, true));
        System.out.println("- race: " + StringUtils.enumToString(race, true));
        System.out.println("- e class: " + StringUtils.enumToString(economicClass, true));
        System.out.println("- personality: " + StringUtils.enumToString(personality.getType(), true));
        System.out.println("- ambition: " + StringUtils.enumToString(ambition, true));
        System.out.println("- morality: " + StringUtils.enumToString(morality, true));
        System.out.println("- role: " + StringUtils.enumToString(role, true));
        System.out.println("- aspirations: " + StringUtils.enumToString(true, aspirations.toArray(new Aspiration[]{})));
        //System.out.println("- aspirations: " + aspirations.get(0).getDescription());
        System.out.println("=========================");
    }

    public List<Text> getMetaDataAsList(){
        List<Text> give = new ArrayList<>();

        give.add(Text.of(firstName + " " + lastName + ": "));
        give.add(Text.of("- age: " + age));
        give.add(Text.of("- gender: " + StringUtils.enumToString(gender, true)));
        give.add(Text.of("- race: " + StringUtils.enumToString(race, true)));
        give.add(Text.of("- e class: " + StringUtils.enumToString(economicClass, true)));
        give.add(Text.of("- personality: " + StringUtils.enumToString(personality.getType(), true)));
        give.add(Text.of("- ambition: " + StringUtils.enumToString(ambition, true)));
        give.add(Text.of("- morality: " + StringUtils.enumToString(morality, true)));
        give.add(Text.of("- role: " + StringUtils.enumToString(role, true)));
        give.add(Text.of("- aspirations: " + StringUtils.enumToString(true, aspirations.toArray(new Aspiration[]{}))));

        for(Relationship relationship: Managers.AI.getRelationships(Managers.AI.findNPC(uuid).get())){
            give.add(Text.of("- relationship: " + Managers.AI.findNPC(relationship.getNpcOne()).get().getMetaData().getFullName() + ", "
                    + Managers.AI.findNPC(relationship.getNpcTwo()).get().getMetaData().getFullName() + ": " + relationship.getTrust()));
        }

        return give;
    }

    public boolean isCompatibleWith(NPC npc2) {
        if(morality.isCompatibleWith(npc2.getMetaData().morality)){
            return true;
        }
        return false;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public Race getRace() {
        return race;
    }

    public EconomicClass getEconomicClass() {
        return economicClass;
    }

    public Node getHome() {
        return home;
    }
}
