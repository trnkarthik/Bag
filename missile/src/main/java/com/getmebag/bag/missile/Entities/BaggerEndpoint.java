package com.getmebag.bag.missile.Entities;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.Nullable;
import javax.inject.Named;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * WARNING: This generated code is intended as a sample or starting point for using a
 * Google Cloud Endpoints RESTful API with an Objectify entity. It provides no data access
 * restrictions and no data validation.
 * <p/>
 * DO NOT deploy this code unchanged as part of a real application to real users.
 */
@Api(
        name = "baggerApi",
        version = "v1",
        resource = "bagger",
        namespace = @ApiNamespace(
                ownerDomain = "Entities.missile.bag.getmebag.com",
                ownerName = "Entities.missile.bag.getmebag.com",
                packagePath = ""
        )
)
public class BaggerEndpoint {

    private static final Logger logger = Logger.getLogger(BaggerEndpoint.class.getName());

    private static final int DEFAULT_LIST_LIMIT = 20;

    static {
        // Typically you would register this inside an OfyServive wrapper. See: https://code.google.com/p/objectify-appengine/wiki/BestPractices
        ObjectifyService.register(Bagger.class);
    }

    /**
     * Returns the {@link Bagger} with the corresponding ID.
     *
     * @param userId the ID of the entity to be retrieved
     * @return the entity with the corresponding ID
     * @throws NotFoundException if there is no {@code Bagger} with the provided ID.
     */
    @ApiMethod(
            name = "get",
            path = "bagger/{userId}",
            httpMethod = ApiMethod.HttpMethod.GET)
    public Bagger get(@Named("userId") long userId) throws NotFoundException {
        logger.info("Getting Bagger with ID: " + userId);
        Bagger bagger = ofy().load().type(Bagger.class).id(userId).now();
        if (bagger == null) {
            throw new NotFoundException("Could not find Bagger with ID: " + userId);
        }
        return bagger;
    }

    /**
     * Inserts a new {@code Bagger}.
     */
    @ApiMethod(
            name = "insert",
            path = "bagger",
            httpMethod = ApiMethod.HttpMethod.POST)
    public Bagger insert(Bagger bagger) {
        // Typically in a RESTful API a POST does not have a known ID (assuming the ID is used in the resource path).
        // You should validate that bagger.userId has not been set. If the ID type is not supported by the
        // Objectify ID generator, e.g. long or String, then you should generate the unique ID yourself prior to saving.
        //
        // If your client provides the ID then you should probably use PUT instead.
        ofy().save().entity(bagger).now();
        logger.info("Created Bagger with ID: " + bagger.getUserId());

        return ofy().load().entity(bagger).now();
    }

    /**
     * Updates an existing {@code Bagger}.
     *
     * @param userId the ID of the entity to be updated
     * @param bagger the desired state of the entity
     * @return the updated version of the entity
     * @throws NotFoundException if the {@code userId} does not correspond to an existing
     *                           {@code Bagger}
     */
    @ApiMethod(
            name = "update",
            path = "bagger/{userId}",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public Bagger update(@Named("userId") long userId, Bagger bagger) throws NotFoundException {
        // TODO: You should validate your ID parameter against your resource's ID here.
        checkExists(userId);
        ofy().save().entity(bagger).now();
        logger.info("Updated Bagger: " + bagger);
        return ofy().load().entity(bagger).now();
    }

    /**
     * Deletes the specified {@code Bagger}.
     *
     * @param userId the ID of the entity to delete
     * @throws NotFoundException if the {@code userId} does not correspond to an existing
     *                           {@code Bagger}
     */
    @ApiMethod(
            name = "remove",
            path = "bagger/{userId}",
            httpMethod = ApiMethod.HttpMethod.DELETE)
    public void remove(@Named("userId") long userId) throws NotFoundException {
        checkExists(userId);
        ofy().delete().type(Bagger.class).id(userId).now();
        logger.info("Deleted Bagger with ID: " + userId);
    }

    /**
     * List all entities.
     *
     * @param cursor used for pagination to determine which page to return
     * @param limit  the maximum number of entries to return
     * @return a response that encapsulates the result list and the next page token/cursor
     */
    @ApiMethod(
            name = "list",
            path = "bagger",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<Bagger> list(@Nullable @Named("cursor") String cursor, @Nullable @Named("limit") Integer limit) {
        limit = limit == null ? DEFAULT_LIST_LIMIT : limit;
        Query<Bagger> query = ofy().load().type(Bagger.class).limit(limit);
        if (cursor != null) {
            query = query.startAt(Cursor.fromWebSafeString(cursor));
        }
        QueryResultIterator<Bagger> queryIterator = query.iterator();
        List<Bagger> baggerList = new ArrayList<Bagger>(limit);
        while (queryIterator.hasNext()) {
            baggerList.add(queryIterator.next());
        }
        return CollectionResponse.<Bagger>builder().setItems(baggerList).setNextPageToken(queryIterator.getCursor().toWebSafeString()).build();
    }

    private void checkExists(long userId) throws NotFoundException {
        try {
            ofy().load().type(Bagger.class).id(userId).safe();
        } catch (com.googlecode.objectify.NotFoundException e) {
            throw new NotFoundException("Could not find Bagger with ID: " + userId);
        }
    }
}