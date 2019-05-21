using BookingPlaceAPI.Entities;
using BookingPlaceAPI.Repositories;
using System;
using System.Threading;
using System.Threading.Tasks;

namespace BookingPlaceAPI.Stores
{
    public class UserStore : BaseStore, IUserStore
    {
        private readonly UserRepository _userRepository;

        public UserStore(string connectionString)
        {
            _userRepository = new UserRepository(connectionString);
        }

        public async Task<ShortUserInfo> GetUserAsync(string phone, string password, CancellationToken cancellationToken)
        {
            cancellationToken.ThrowIfCancellationRequested();
            ThrowIfDisposed();
            if (string.IsNullOrEmpty(phone))
                throw new ArgumentNullException(nameof(phone));
            if (string.IsNullOrEmpty(password))
                throw new ArgumentNullException(nameof(password));

            return await _userRepository.GetUserAsync(phone, password, cancellationToken);
        }

        public async Task<ShortUserInfo> GetUserAsync(int userId, CancellationToken cancellationToken)
        {
            cancellationToken.ThrowIfCancellationRequested();
            ThrowIfDisposed();
            if (userId <= 0)
                throw new ArgumentException(nameof(userId));

            return await _userRepository.GetUserAsync(userId, cancellationToken);
        }

        public async Task<ShortUserInfo> GetUserByPhoneAsync(string phone, CancellationToken cancellationToken)
        {
            cancellationToken.ThrowIfCancellationRequested();
            ThrowIfDisposed();
            if (string.IsNullOrEmpty(phone))
                throw new ArgumentNullException(nameof(phone));

            return await _userRepository.GetUserByPhoneAsync(phone, cancellationToken);
        }

        public async Task<UserEntity> CreateUserAsync(UserEntity entity, CancellationToken cancellationToken)
        {
            cancellationToken.ThrowIfCancellationRequested();
            ThrowIfDisposed();
            if (entity == null)
                throw new ArgumentNullException(nameof(entity));

            return await _userRepository.CreateUserAsync(entity, cancellationToken);
        }
    }
}
