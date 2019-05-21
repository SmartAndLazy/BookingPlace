using BookingPlaceAPI.Entities;
using BookingPlaceAPI.Enums;
using BookingPlaceAPI.Models;
using System;
using System.Collections.Generic;
using System.Threading;
using System.Threading.Tasks;

namespace BookingPlaceAPI.Stores
{
    public interface IOrganizationStore : IDisposable
    {
        Task<IEnumerable<OrganizationSocialNetworkUrlEntity>> GetOrganizationSocialNetworkAccountsAsync(int organizationId, CancellationToken cancellationToken = default);
        Task<OrganizationEntity> GetOrganizationAsync(int organizationId, CancellationToken cancellationToken = default);
        Task<OrganizationEntity> GetOrganizationAsync(string name, double latitude, double longitude, CancellationToken cancellationToken = default);
        Task<OrganizationEntity> CreateOrganizationAsync(string name, double latitude, double longitude, YandexCompanyMetaData companyMetaData, CancellationToken cancellationToken = default);

        Task UpdateRatingAsync(int organizationId, double rating, CancellationToken cancellationToken = default);
        Task UpdateDescriptionAsync(int organizationId, string description, CancellationToken cancellationToken = default);
        Task UpdateDescriptionAsync(string name, double latitude, double longitude, string description, CancellationToken cancellationToken = default);

        Task AddSocialNetworkAccountAsync(int organizationId, SocialNetworkType socialNetworkType, string url, CancellationToken cancellationToken = default);
        Task AddSocialNetworkAccountAsync(OrganizationSocialNetworkUrlShortModel organizationSN, CancellationToken cancellationToken = default);

        Task Rate(RatingEntity entity, CancellationToken cancellationToken = default);
        Task<IEnumerable<UserRatingsEntity>> GetRatingByOrganization(int organizationId, CancellationToken cancellationToken = default);

        Task<OrganizationBookingPlaceEntity> BookingPlace(OrganizationBookingPlaceEntity entity, CancellationToken cancellationToken = default);
        Task<IEnumerable<OrganizationBookingPlaceEntity>> GetOrganizationBookingPlacement(int organizationId, DateTime date, CancellationToken cancellationToken = default);
    }
}
