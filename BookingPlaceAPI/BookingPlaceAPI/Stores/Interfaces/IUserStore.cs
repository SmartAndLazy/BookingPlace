using BookingPlaceAPI.Entities;
using System;
using System.Threading;
using System.Threading.Tasks;

namespace BookingPlaceAPI.Stores
{
    public interface IUserStore : IDisposable
    {
        Task<ShortUserInfo> GetUserAsync(string phone, string password, CancellationToken cancellationToken = default);
        Task<ShortUserInfo> GetUserAsync(int userId, CancellationToken cancellationToken = default);
        Task<ShortUserInfo> GetUserByPhoneAsync(string phone, CancellationToken cancellationToken = default);
        Task<UserEntity> CreateUserAsync(UserEntity entity, CancellationToken cancellationToken = default);
    }
}
