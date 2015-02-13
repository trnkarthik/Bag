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
public class BagUserEndpoint {

    private static final Logger logger = Logger.getLogger(BagUserEndpoint.class.getName());

    private static final int DEFAULT_LIST_LIMIT = 20;

    static {
        // Typically you would register this inside an OfyServive wrapper. See: https://code.google.com/p/objectify-appengine/wiki/BestPractices
        ObjectifyService.register(BagUser.class);
    }

    /**
     * Returns the {@link BagUser} with the corresponding ID.
     *
     * @param userId the ID of the entity to be retrieved
     * @return the entity with the corresponding ID
     * @throws NotFoundException if there is no {@code Bagger} with the provided ID.
     */
    @ApiMethod(
            name = "get",
            path = "bagger/{userId}",
            httpMethod = ApiMethod.HttpMethod.GET)
    public BagUser get(@Named("userId") long userId) throws NotFoundException {
        logger.info("Getting Bagger with ID: " + userId);
        BagUser bagUser = ofy().load().type(BagUser.class).id(userId).now();
        if (bagUser == null) {
            throw new NotFoundException("Could not find Bagger with ID: " + userId);
        }
        return bagUser;
    }

    /**
     * Inserts a new {@code Bagger}.
     */
    @ApiMethod(
            name = "insert",
            path = "bagger",
            httpMethod = ApiMethod.HttpMethod.POST)
    public BagUser insert(BagUser bagUser) {
        // Typically in a RESTful API a POST does not have a known ID (assuming the ID is used in the resource path).
        // You should validate that bagger.userId has not been set. If the ID type is not supported by the
        // Objectify ID generator, e.g. long or String, then you should generate the unique ID yourself prior to saving.
        //
        // If your client provides the ID then you should probably use PUT instead.
        ofy().save().entity(bagUser).now();
        logger.info("Created Bagger with ID: " + bagUser.getUserId());

        return ofy().load().entity(bagUser).now();
    }

    /**
     * Updates an existing {@code Bagger}.
     *
     * @param userId the ID of the entity to be updated
     * @param bagUser the desired state of the entity
     * @return the updated version of the entity
     * @throws NotFoundException if the {@code userId} does not correspond to an existing
     *                           {@code Bagger}
     */
    @ApiMethod(
            name = "update",
            path = "bagger/{userId}",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public BagUser update(@Named("userId") long userId, BagUser bagUser) throws NotFoundException {
        // TODO: You should validate your ID parameter against your resource's ID here.
        checkExists(userId);
        ofy().save().entity(bagUser).now();
        logger.info("Updated Bagger: " + bagUser);
        return ofy().load().entity(bagUser).now();
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
        ofy().delete().type(BagUser.class).id(userId).now();
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
    public CollectionResponse<BagUser> list(@Nullable @Named("cursor") String cursor, @Nullable @Named("limit") Integer limit) {
        limit = limit == null ? DEFAULT_LIST_LIMIT : limit;
        Query<BagUser> query = ofy().load().type(BagUser.class).limit(limit);
        if (cursor != null) {
            query = query.startAt(Cursor.fromWebSafeString(cursor));
        }
        QueryResultIterator<BagUser> queryIterator = query.iterator();
        List<BagUser> bagUserList = new ArrayList<BagUser>(limit);
        while (queryIterator.hasNext()) {
            bagUserList.add(queryIterator.next());
        }
        return CollectionResponse.<BagUser>builder().setItems(bagUserList).setNextPageToken(queryIterator.getCursor().toWebSafeString()).build();
    }

    private void checkExists(long userId) throws NotFoundException {
        try {
            ofy().load().type(BagUser.class).id(userId).safe();
        } catch (com.googlecode.objectify.NotFoundException e) {
            throw new NotFoundException("Could not find Bagger with ID: " + userId);
        }
    }
}