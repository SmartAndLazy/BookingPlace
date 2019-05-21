using BookingPlaceAPI.Entities;
using Dapper;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading;
using System.Threading.Tasks;

namespace BookingPlaceAPI.Repositories
{
    public class OrganizationRepository : BaseRepository
    {
        public OrganizationRepository(string connectionString) : base(connectionString) { }

        public async Task<OrganizationEntity> GetOrganizationAsync(string name, double latitude, double longitude, CancellationToken cancellationToken)
        {
            var sqlCommand = new CommandDefinition($@"
                select top 1 *
                from [dbo].[Organization]
                where [Name] = @name and [Latitude] = @latitude and [Longitude] = @longitude",
                new
                {
                    @name = name,
                    @latitude = latitude,
                    @longitude = longitude
                },
                cancellationToken: cancellationToken);
            try
            {
                return await Db.QueryFirstOrDefaultAsync(typeof(OrganizationEntity), sqlCommand) as OrganizationEntity;
            }
            catch (Exception ex)
            {
                throw;
            }
        }

        public async Task<OrganizationEntity> GetOrganizationAsync(int organizationId, CancellationToken cancellationToken)
        {
            var sqlCommand = new CommandDefinition($@"
                select top 1 *
                from [dbo].[Organization]
                where [Id] = @organizationId",
                new { @organizationId = organizationId},
                cancellationToken: cancellationToken);
            try
            {
                return await Db.QueryFirstOrDefaultAsync(typeof(OrganizationEntity), sqlCommand) as OrganizationEntity;
            }
            catch (Exception ex)
            {
                throw;
            }
        }

        public async Task<OrganizationEntity> CreateOrganizationAsync(OrganizationEntity entity, CancellationToken cancellationToken)
        {
            var sqlCommand = new CommandDefinition(@"
                insert [dbo].[Organization] (
                    Name,
                    RuName,
                    Latitude,
                    Longitude,
                    Address,
                    Rating,
                    WorkingStartHour,
                    WorkingStartMinute,
                    WorkingEndHour,
                    WorkingEndMinute,
                    Description,
                    Phone,
                    CountOfAvailablePlacement,
                    Categories,
                    WorkingTimeStr,
                    Url)
                values (
                    @name,
                    @ruName,
                    @latitude,
                    @longitude,
                    @address,
                    @rating,
                    @workingStartHour,
                    @workingStartMinute,
                    @workingEndHour,
                    @workingEndMinute,
                    @description,
                    @phone,
                    @countOfAvailablePlacement,
                    @categories,
                    @workingTimeStr,
                    @url);

                SELECT CAST(SCOPE_IDENTITY() as int)",
                new
                {
                    @name = entity.Name,
                    @ruName = entity.RuName,
                    @latitude = entity.Latitude,
                    @longitude = entity.Longitude,
                    @address = entity.Address,
                    @rating = entity.Rating,
                    @workingStartHour = entity.WorkingStartHour,
                    @workingStartMinute = entity.WorkingStartMinute,
                    @workingEndHour = entity.WorkingEndHour,
                    @workingEndMinute = entity.WorkingEndMinute,
                    @description = entity.Description,
                    @phone = entity.Phone,
                    @countOfAvailablePlacement = entity.CountOfAvailablePlacement,
                    @categories = entity.Categories,
                    @workingTimeStr = entity.WorkingTimeStr,
                    @url = entity.Url
                },
                cancellationToken: cancellationToken);
            try
            {
                entity.Id = (await Db.QueryFirstOrDefaultAsync(typeof(int), sqlCommand) as int?).Value;
                return entity;
            }
            catch (Exception ex)
            {
                throw;
            }
        }

        public async Task<IEnumerable<OrganizationSocialNetworkUrlEntity>> GetOrganizationSocialNetworkAccountsAsync(int organizationId, CancellationToken cancellationToken)
        {
            var sqlCommand = new CommandDefinition($@"
                select *
                from [dbo].[OrganizationSocialNetworkUrls]
                where [OrganizationId] = @organizationId",
                new { @organizationId = organizationId },
                cancellationToken: cancellationToken);
            try
            {
                return await Db.QueryAsync<OrganizationSocialNetworkUrlEntity>(sqlCommand);
            }
            catch (Exception ex)
            {
                return Enumerable.Empty<OrganizationSocialNetworkUrlEntity>();
            }
        }

        public async Task UpdateRatingAsync(int organizationId, double rating, CancellationToken cancellationToken)
        {
            var sqlCommand = new CommandDefinition(@"
                UPDATE [dbo].[Organization]
                SET [Rating] = @rating
                WHERE [Id] = @id",
                new
                {
                    @id = organizationId,
                    @rating = rating
                },
                cancellationToken: cancellationToken);

            try
            {
                await Db.ExecuteAsync(sqlCommand);
            }
            catch (Exception ex)
            {
                throw;
            }
        }

        public async Task UpdateDescriptionAsync(int organizationId, string description, CancellationToken cancellationToken)
        {
            var sqlCommand = new CommandDefinition(@"
                UPDATE [dbo].[Organization]
                SET [Description] = @description
                WHERE [Id] = @id",
               new
               {
                   @id = organizationId,
                   @description = description
               },
               cancellationToken: cancellationToken);

            try
            {
                await Db.ExecuteAsync(sqlCommand);
            }
            catch (Exception ex)
            {
                throw;
            }
        }

        public async Task AddSocialNetworkAccountAsync(OrganizationSocialNetworkUrlEntity entity, CancellationToken cancellationToken)
        {
            var sqlCommand = new CommandDefinition(@"
                insert [dbo].[OrganizationSocialNetworkUrls] (OrganizationId, Url, Type)
                values(@organizationId, @url, @type)",
                new
                {
                    @organizationId = entity.OrganizationId,
                    @url = entity.Url,
                    @type = entity.Type
                },
                cancellationToken: cancellationToken);
            try
            {
                await Db.ExecuteAsync(sqlCommand);
            }
            catch (Exception ex)
            {
                throw;
            }
        }

        public async Task Rate(RatingEntity entity, CancellationToken cancellationToken)
        {
            var sqlCommand = new CommandDefinition(@"
                insert [dbo].[Ratings] (OrganizationId, UserId, Rating, Text)
                values(@organizationId, @userId, @rating, @text)",
                new
                {
                    @organizationId = entity.OrganizationId,
                    @userId = entity.UserId,
                    @rating = entity.Rating,
                    @text = entity.Text
                },
                cancellationToken: cancellationToken);
            try
            {
                await Db.ExecuteAsync(sqlCommand);
            }
            catch (Exception ex)
            {
                throw;
            }
        }

        public async Task<IEnumerable<UserRatingsEntity>> GetRatingByOrganization(int organizationId, CancellationToken cancellationToken)
        {
            var sqlCommand = new CommandDefinition($@"
                select
	                ratings.[OrganizationId],
	                ratings.[UserId],
	                ratings.[Rating],
	                ratings.[Text],
	                users.[Name],
	                users.[Surname],
	                users.[Phone]
                from [dbo].[Ratings] as ratings
                join [dbo].[Users] as users on ratings.[UserId] = users.[Id]
                where ratings.[OrganizationId] = @organizationId",
                new { @organizationId = organizationId },
                cancellationToken: cancellationToken);
            try
            {
                return await Db.QueryAsync<UserRatingsEntity>(sqlCommand);
            }
            catch (Exception ex)
            {
                return Enumerable.Empty<UserRatingsEntity>();
            }
        }

        public async Task<OrganizationBookingPlaceEntity> BookingPlace(OrganizationBookingPlaceEntity entity, CancellationToken cancellationToken)
        {
            var sqlCommand = new CommandDefinition(@"
                insert [dbo].[OrganizationBookingPlace] (
                    OrganizationId,
                    Date,
                    Phone,
                    Name,
                    Surname,
                    BookingStartHour,
                    BookingStartMinute,
                    NumberOfTable)
                values (
                    @organizationId,
                    @date,
                    @phone,
                    @name,
                    @surname,
                    @bookingStartHour,
                    @bookingStartMinute,
                    @numberOfTable);

                SELECT CAST(SCOPE_IDENTITY() as int)",
                new
                {
                    @organizationId = entity.OrganizationId,
                    @date = entity.Date,
                    @phone = entity.Phone,
                    @name = entity.Name,
                    @surname = entity.Surname,
                    @bookingStartHour = entity.BookingStartHour,
                    @bookingStartMinute = entity.BookingStartMinute,
                    @numberOfTable = entity.NumberOfTable
                },
                cancellationToken: cancellationToken);
            try
            {
                entity.Id = (await Db.QueryFirstOrDefaultAsync(typeof(int), sqlCommand) as int?).Value;
                return entity;
            }
            catch (Exception ex)
            {
                throw;
            }
        }

        public async Task<IEnumerable<OrganizationBookingPlaceEntity>> GetOrganizationBookingPlacement(int organizationId, DateTime date, CancellationToken cancellationToken)
        {
            var sqlCommand = new CommandDefinition($@"
                select *
                from [dbo].[OrganizationBookingPlace]
                where [OrganizationId] = @organizationId and [Date] = @date",
                new
                {
                    @organizationId = organizationId,
                    @date = date.Date
                },
                cancellationToken: cancellationToken);
            try
            {
                return await Db.QueryAsync<OrganizationBookingPlaceEntity>(sqlCommand);
            }
            catch (Exception ex)
            {
                return Enumerable.Empty<OrganizationBookingPlaceEntity>();
            }
        }
    }
}
