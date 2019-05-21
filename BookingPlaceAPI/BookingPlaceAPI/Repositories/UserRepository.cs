using BookingPlaceAPI.Entities;
using Dapper;
using System;
using System.Threading;
using System.Threading.Tasks;

namespace BookingPlaceAPI.Repositories
{
    public class UserRepository : BaseRepository
    {
        public UserRepository(string connectionString) : base(connectionString) { }

        public async Task<ShortUserInfo> GetUserAsync(string phone, string password, CancellationToken cancellationToken)
        {
            var sqlCommand = new CommandDefinition($@"
                select top 1 *
                from [dbo].[Users]
                where [Phone] = @phone and [Password] = @password",
                new { @phone = phone, @password = password },
                cancellationToken: cancellationToken);
            try
            {
                return await Db.QueryFirstOrDefaultAsync(typeof(ShortUserInfo), sqlCommand) as ShortUserInfo;
            }
            catch (Exception ex)
            {
                throw;
            }
        }

        public async Task<ShortUserInfo> GetUserAsync(int userId, CancellationToken cancellationToken)
        {
            var sqlCommand = new CommandDefinition($@"
                select top 1 *
                from [dbo].[Users]
                where [Id] = @userId",
                new { @userId = userId },
                cancellationToken: cancellationToken);
            try
            {
                return await Db.QueryFirstOrDefaultAsync(typeof(ShortUserInfo), sqlCommand) as ShortUserInfo;
            }
            catch (Exception ex)
            {
                throw;
            }
        }

        public async Task<ShortUserInfo> GetUserByPhoneAsync(string phone, CancellationToken cancellationToken)
        {
            var sqlCommand = new CommandDefinition($@"
                select top 1 *
                from [dbo].[Users]
                where [Phone] = @phone",
                new { @phone = phone },
                cancellationToken: cancellationToken);
            try
            {
                return await Db.QueryFirstOrDefaultAsync(typeof(ShortUserInfo), sqlCommand) as ShortUserInfo;
            }
            catch (Exception ex)
            {
                throw;
            }
        }

        public async Task<UserEntity> CreateUserAsync(UserEntity entity, CancellationToken cancellationToken)
        {
            var sqlCommand = new CommandDefinition(@"
                insert [dbo].[Users] (
                    Phone,
                    Password,
                    Name,
                    Surname)
                values (
                    @phone,
                    @password,
                    @name,
                    @surname);

                SELECT CAST(SCOPE_IDENTITY() as int)",
                new
                {
                    @phone = entity.Phone,
                    @password = entity.Password,
                    @name = entity.Name,
                    @surname = entity.Surname
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
    }
}
