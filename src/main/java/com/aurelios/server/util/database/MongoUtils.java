package com.aurelios.server.util.database;

import com.aurelios.server.game.environment.nodes.Node;
import com.aurelios.server.game.environment.nodes.PointOfInterest;
import com.aurelios.server.game.environment.nodes.shapes.Shape;
import com.aurelios.server.game.environment.nodes.shapes.ShapeCircle;
import com.aurelios.server.game.environment.nodes.shapes.ShapeRectangle;
import com.aurelios.server.game.user.ai.AIMetaData;
import com.aurelios.server.game.user.ai.NPC;
import com.aurelios.server.game.user.ai.traits.*;
import com.aurelios.server.game.user.ai.traits.personality.Personalities;
import com.aurelios.server.game.user.ai.traits.personality.Personality;
import com.aurelios.server.managers.Managers;
import com.aurelios.server.util.misc.StringUtils;
import com.aurelios.server.util.misc.UUIDUtils;
import com.flowpowered.math.vector.Vector3d;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.spongepowered.api.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.mongodb.client.model.Filters.eq;

public class MongoUtils {

    public static final String COLLECTION_NODES = "nodes";
    public static final String COLLECTION_NPCS = "npcs";

    private MongoClient client;
    private MongoDatabase database;
    private String username, password, ip;

    public MongoUtils(String username, String password, String ip) {
        this.username = username;
        this.password = password;
        this.ip = ip;
    }

    public void openConnection(){
        try {
            MongoClientURI uri = new MongoClientURI("mongodb://" + username + ":" + password + ip);
            client = new MongoClient(uri);
            if (database == null) {
                database = client.getDatabase("aurelios");
            }
        } catch( Exception e){
            System.out.println(e.toString());
        }
    }

    public boolean isConnected(){
        return client != null;
    }

    public MongoDatabase getDatabase(){
        return database;
    }

    public void close() {
        writeNodes();
        writeNPCS();
        client.close();
        client = null;
        database = null;
    }

    /**
     * Use one of the public static final strings at the top of this class
     * @param collection
     */
    public void loadData(String collection){
        switch (collection){
            case COLLECTION_NODES: loadNodes();
                break;
            case COLLECTION_NPCS: loadNPCS();
        }
    }

    private void loadNPCS() {
        /**
         * Document add = new Document("uuid", metaData.getUuid().toString());
         add.append("name", metaData.getFullName()).append("home", metaData.getHome().getDisplayName().toPlain())
         .append("age", metaData.getAge()).append("gender", StringUtils.enumToString(metaData.getGender()))
         .append("personality", StringUtils.enumToString(metaData.getPersonality().getType()))
         .append("ambition", StringUtils.enumToString(metaData.getAmbition()))
         .append("role", StringUtils.enumToString(metaData.getRole()))
         .append("morality", StringUtils.enumToString(metaData.getMorality()))
         .append("race", StringUtils.enumToString(metaData.getRace()))
         .append("economicClass", StringUtils.enumToString(metaData.getEconomicClass()))
         .append("aspirations", StringUtils.enumToString(metaData.getAspirations()
         .toArray(new Aspiration[metaData.getAspirations().size()])));
         */

        MongoCollection<Document> npcs = database.getCollection(COLLECTION_NPCS);

        npcs.find().forEach(new Block<Document>() {
            @Override
            public void apply(Document document) {
                System.out.println("APPLY");
                String[] temp = document.getString("name").split(" ");
                String[] as = document.getString("aspirations").split(",");
                List<Aspiration> aspirations = new CopyOnWriteArrayList<>();
                for(String a: as){
                    aspirations.add(Aspiration.valueOf(a.trim().toUpperCase()));
                }

                AIMetaData metaData = new AIMetaData(Managers.NODES.findNode(UUID.fromString(document.getString("home"))).get(),
                        UUID.fromString(document.getString("uuid")),
                        temp[0],
                        temp[1],
                        document.getInteger("age"),
                        Gender.valueOf(document.getString("gender").toUpperCase()),
                        Personality.getPersonalityFromType(Personalities.valueOf(document.getString("personality").toUpperCase())),
                        Ambition.valueOf(document.getString("ambition").toUpperCase()),
                        Role.valueOf(document.getString("role").toUpperCase()),
                        Morality.valueOf(document.getString("morality").toUpperCase()),
                        Race.valueOf(document.getString("race").toUpperCase()),
                        EconomicClass.valueOf(document.getString("economicClass").toUpperCase()),
                        aspirations);
                Managers.AI.add(new NPC(metaData));
            }
        });

        for(NPC npc: Managers.AI.getObjects()){
            System.out.println(npc.getMetaData().getFullName() + ": " + npc.getMetaData().getHome().getDisplayName().toPlain());
        }
    }

    private void loadNodes() {
        /**
         * Format for storing/loading nodes:
         * - name
         * - shape (0 = circle, 1 = rectangle)
         * - center/bottom left corner
         * - top right corner
         */
        MongoCollection<Document> areas = database.getCollection(COLLECTION_NODES);

        areas.find().forEach(new Block<Document>() {
            @Override
            public void apply(Document document) {
                Shape shape;
                if(document.getInteger("shape") == 0){
                    String[] temp = document.getString("center").split(",");
                    Vector3d v = new Vector3d(Double.valueOf(temp[0]), Double.valueOf(temp[1]), Double.valueOf(temp[2]));
                    shape = new ShapeCircle(v, document.getDouble("radius"), document.getBoolean("ignoreY"));
                } else {
                    String[] left = document.getString("leftCorner").split(",");
                    Vector3d l = new Vector3d(Double.valueOf(left[0]), Double.valueOf(left[1]), Double.valueOf(left[2]));
                    String[] right = document.getString("rightCorner").split(",");
                    Vector3d r = new Vector3d(Double.valueOf(right[0]), Double.valueOf(right[1]), Double.valueOf(right[2]));
                    shape = new ShapeRectangle(l, r);
                }

                Node area = new Node(UUID.fromString(document.getString("uuid")),
                        Text.of(document.getString("name")), shape, document.getInteger("aiCap"));
                Managers.NODES.add(area);

                if(document.get("children") != null) {
                    for (Document child : (List<Document>) document.get("children")) {
                        Shape childShape;
                        if (child.getInteger("shape") == 0) {
                            String[] temp = child.getString("center").split(",");
                            Vector3d v = new Vector3d(Double.valueOf(temp[0]), Double.valueOf(temp[1]), Double.valueOf(temp[2]));
                            childShape = new ShapeCircle(v, child.getDouble("radius"), child.getBoolean("ignoreY"));
                        } else {
                            String[] left = child.getString("leftCorner").split(",");
                            Vector3d l = new Vector3d(Double.valueOf(left[0]), Double.valueOf(left[1]), Double.valueOf(left[2]));
                            String[] right = child.getString("rightCorner").split(",");
                            Vector3d r = new Vector3d(Double.valueOf(right[0]), Double.valueOf(right[1]), Double.valueOf(right[2]));
                            childShape = new ShapeRectangle(l, r);
                        }
                        area.addChild(new PointOfInterest(UUID.fromString(document.getString("uuid")), Text.of(child.getString("name")), childShape, area,
                                child.get("roles") == null ? null : child.getString("roles")));
                    }
                }
            }
        });
    }

    private void writeNPCS(){
        /**
         * Format:
         * - uuid
         * - name (first and last)
         * - Node home;
         - age;
         - gender;
         - personality;
         - ambition;
         - role;
         - morality;
         - List<Aspiration> aspirations;
         - race;
         - economicClass;
         */
        List<Document> temp = new ArrayList<>();
        MongoCollection<Document> col = database.getCollection(COLLECTION_NPCS);

        for(NPC npc: Managers.AI.getObjects()){
            //TODO update existing ones here
            AIMetaData metaData = npc.getMetaData();
            Document add = new Document("uuid", metaData.getUuid().toString());
            add.append("name", metaData.getFullName()).append("home", metaData.getHome().getUuid().toString())
                    .append("age", metaData.getAge()).append("gender", StringUtils.enumToString(metaData.getGender(), false))
                    .append("personality", StringUtils.enumToString(metaData.getPersonality().getType(), false))
                    .append("ambition", StringUtils.enumToString(metaData.getAmbition(), false))
                    .append("role", StringUtils.enumToString(metaData.getRole(), false))
                    .append("morality", StringUtils.enumToString(metaData.getMorality(), false))
                    .append("race", StringUtils.enumToString(metaData.getRace(), false))
                    .append("economicClass", StringUtils.enumToString(metaData.getEconomicClass(), false))
                    .append("aspirations", StringUtils.enumToString(false, metaData.getAspirations()
                            .toArray(new Aspiration[metaData.getAspirations().size()])));

            temp.add(add);

        }

        if(temp.size() > 0){
            col.insertMany(temp);
        }

    }

    private void writeNodes(){
        /**
         * Format for storing/loading nodes:
         * - name
         * - shape (0 = circle, 1 = rectangle)
         * - center/left corner
         * - right corner
         * - children (embedded document with same format as node minus children option)
         */
        List<Document> temp = new ArrayList<>();
        MongoCollection<Document> col = database.getCollection(COLLECTION_NODES);

        for(Node a: Managers.NODES.getObjects()){
            if(col.find(eq("name", a.getDisplayName().toPlain())).first() != null){
                //already exists
                continue;
            }

            List<Document> children = new ArrayList<>();
            if(a.getChildren().size() > 0){
                for(PointOfInterest p: a.getChildren()){
                    Document add = new Document("name", p.getDisplayName().toPlain()).append("uuid", p.getUuid().toString());

                    if(p.getShape() instanceof ShapeCircle){
                        add.append("shape", 0)
                                .append("center", ((ShapeCircle)p.getShape()).getCenterAsString())
                                .append("radius", ((ShapeCircle)p.getShape()).getRadius())
                                .append("ignoreY", ((ShapeCircle)p.getShape()).ignoreY());
                    } else {
                        add.append("shape", 1)
                                .append("leftCorner", ((ShapeRectangle)p.getShape()).getBottomLeftAsString())
                                .append("rightCorner", ((ShapeRectangle)p.getShape()).getTopRightAsString());

                    }
                    if(p.getAvailableRoles() != null) {
                        add.append("roles", p.getAvailableRolesAsString());
                    }
                    children.add(add);
                }
            }

            if(a.getShape() instanceof ShapeCircle){
                temp.add(new Document("name", a.getDisplayName().toPlain()).append("uuid", a.getUuid().toString())
                        .append("shape", 0)
                        .append("center", ((ShapeCircle)a.getShape()).getCenterAsString())
                        .append("radius", ((ShapeCircle)a.getShape()).getRadius())
                        .append("ignoreY", ((ShapeCircle)a.getShape()).ignoreY())
                        .append("aiCap", a.getAiCap())
                        .append("children", children));

            } else {
                temp.add(new Document("name", a.getDisplayName().toPlain())
                        .append("shape", 1)
                        .append("leftCorner", ((ShapeRectangle)a.getShape()).getBottomLeftAsString())
                        .append("rightCorner", ((ShapeRectangle)a.getShape()).getTopRightAsString())
                        .append("aiCap", a.getAiCap())
                        .append("children", children));
            }
        }

        if(temp.size() > 0) {
            col.insertMany(temp);
        }
    }

    public Document fetchPlayerData(UUID uuid){
        FindIterable<Document> iterable = null;
        if (!isConnected())
            openConnection();
        if (database != null) {
            iterable = database.getCollection("players").find(eq("uuid", UUIDUtils.getRawUUID(uuid)));
            Document document = iterable.first();
            return document;
        }
        return null;
    }

    public Document fetchEmbeddedDocument(Document document, String field) { //for reading a single, non-embedded integer
        return (Document)document.get(field);
    }
}
