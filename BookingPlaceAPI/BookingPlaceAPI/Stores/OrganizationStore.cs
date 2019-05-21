using BookingPlaceAPI.Entities;
using BookingPlaceAPI.Enums;
using BookingPlaceAPI.Helpers;
using BookingPlaceAPI.Models;
using BookingPlaceAPI.Repositories;
using BookingPlaceAPI.ViewModels;
using System;
using System.Collections.Generic;
using System.Threading;
using System.Threading.Tasks;

namespace BookingPlaceAPI.Stores
{
    public class OrganizationStore : BaseStore, IOrganizationStore
    {
        private readonly OrganizationRepository _organizationRepository;

        public OrganizationStore(string connectionString)
        {
            _organizationRepository = new OrganizationRepository(connectionString);
        }

        public async Task<IEnumerable<OrganizationSocialNetworkUrlEntity>> GetOrganizationSocialNetworkAccountsAsync(int organizationId, CancellationToken cancellationToken)
        {
            cancellationToken.ThrowIfCancellationRequested();
            ThrowIfDisposed();
            if (organizationId <= 0)
                throw new ArgumentException(nameof(organizationId));

            return await _organizationRepository.GetOrganizationSocialNetworkAccountsAsync(organizationId, cancellationToken);
        }

        public async Task<OrganizationEntity> GetOrganizationAsync(int organizationId, CancellationToken cancellationToken)
        {
            cancellationToken.ThrowIfCancellationRequested();
            ThrowIfDisposed();
            if (organizationId <= 0)
                throw new ArgumentException(nameof(organizationId));

            return await _organizationRepository.GetOrganizationAsync(organizationId, cancellationToken);
        }

        public async Task<OrganizationEntity> GetOrganizationAsync(string name, double latitude, double longitude, CancellationToken cancellationToken)
        {
            cancellationToken.ThrowIfCancellationRequested();
            ThrowIfDisposed();
            if (string.IsNullOrEmpty(name))
                throw new ArgumentNullException(nameof(name));

            return await _organizationRepository.GetOrganizationAsync(name, latitude, longitude, cancellationToken);
        }

        public async Task<OrganizationEntity> CreateOrganizationAsync(string name, double latitude, double longitude, YandexCompanyMetaData companyMetaData, CancellationToken cancellationToken)
        {
            cancellationToken.ThrowIfCancellationRequested();
            ThrowIfDisposed();
            if (string.IsNullOrEmpty(name))
                throw new ArgumentNullException(nameof(name));

            return await _organizationRepository.CreateOrganizationAsync(new OrganizationEntity
            {
                Name = name,
                RuName = companyMetaData?.ShortName ?? name,
                Latitude = latitude,
                Longitude = longitude,
                Address = companyMetaData?.Address ?? $"{latitude} - {longitude}",
                Rating = null,
                WorkingStartHour = TimeGenerator.GetRandomStartHour(),
                WorkingStartMinute = 0,
                WorkingEndHour = TimeGenerator.GetRandomEndHour(),
                WorkingEndMinute = 0,
                Description = DescriptionCreator.GetRandomDiscription(),
                Phone = companyMetaData?.GetPhone() ?? PhoneGenerator.GetRandomPhone(),
                CountOfAvailablePlacement = PlacementGenerator.GetRandomCountOfAvailableOlacement(),
                Categories = companyMetaData?.GetCategories() ?? string.Empty,
                WorkingTimeStr = companyMetaData?.Hours?.Text ?? string.Empty,
                Url = companyMetaData?.Url ?? string.Empty
            }, cancellationToken);
        }

        public async Task UpdateRatingAsync(int organizationId, double rating, CancellationToken cancellationToken)
        {
            cancellationToken.ThrowIfCancellationRequested();
            ThrowIfDisposed();
            if (organizationId <= 0)
                throw new ArgumentException(nameof(organizationId));

            await _organizationRepository.UpdateRatingAsync(organizationId, rating, cancellationToken);
        }

        public async Task UpdateDescriptionAsync(int organizationId, string description, CancellationToken cancellationToken)
        {
            cancellationToken.ThrowIfCancellationRequested();
            ThrowIfDisposed();
            if (organizationId <= 0)
                throw new ArgumentException(nameof(organizationId));
            if (string.IsNullOrEmpty(description))
                throw new ArgumentNullException(nameof(description));

            await _organizationRepository.UpdateDescriptionAsync(organizationId, description, cancellationToken);
        }

        public async Task UpdateDescriptionAsync(string name, double latitude, double longitude, string description, CancellationToken cancellationToken)
        {
            cancellationToken.ThrowIfCancellationRequested();
            ThrowIfDisposed();
            if (string.IsNullOrEmpty(name))
                throw new ArgumentNullException(nameof(name));

            var organization = await _organizationRepository.GetOrganizationAsync(name, latitude, longitude, cancellationToken);
            if (organization == null)
                return;

            await _organizationRepository.UpdateDescriptionAsync(organization.Id, description, cancellationToken);
        }

        public async Task AddSocialNetworkAccountAsync(int organizationId, SocialNetworkType socialNetworkType, string url, CancellationToken cancellationToken)
        {
            cancellationToken.ThrowIfCancellationRequested();
            ThrowIfDisposed();
            if (organizationId <= 0)
                throw new ArgumentException(nameof(organizationId));

            await _organizationRepository.AddSocialNetworkAccountAsync(new OrganizationSocialNetworkUrlEntity
            {
                OrganizationId = organizationId,
                Type = socialNetworkType,
                Url = url
            }, cancellationToken);
        }

        public async Task AddSocialNetworkAccountAsync(OrganizationSocialNetworkUrlShortModel organizationSN, CancellationToken cancellationToken)
        {
            cancellationToken.ThrowIfCancellationRequested();
            ThrowIfDisposed();
            if (organizationSN == null)
                throw new ArgumentNullException(nameof(organizationSN));

            var organization = await _organizationRepository.GetOrganizationAsync(organizationSN.Name, organizationSN.Latitude, organizationSN.Longitude, cancellationToken);
            if (organization == null)
                return;

            await _organizationRepository.AddSocialNetworkAccountAsync(new OrganizationSocialNetworkUrlEntity
            {
                OrganizationId = organization.Id,
                Url = organizationSN.Url,
                Type = organizationSN.Type
            }, cancellationToken);
        }

        public async Task Rate(RatingEntity entity, CancellationToken cancellationToken)
        {
            cancellationToken.ThrowIfCancellationRequested();
            ThrowIfDisposed();
            if (entity == null)
                throw new ArgumentNullException(nameof(entity));

            await _organizationRepository.Rate(entity, cancellationToken);
        }

        public async Task<IEnumerable<UserRatingsEntity>> GetRatingByOrganization(int organizationId, CancellationToken cancellationToken)
        {
            cancellationToken.ThrowIfCancellationRequested();
            ThrowIfDisposed();
            if (organizationId <= 0)
                throw new ArgumentException(nameof(organizationId));

            return await _organizationRepository.GetRatingByOrganization(organizationId, cancellationToken);
        }




        public async Task<OrganizationBookingPlaceEntity> BookingPlace(OrganizationBookingPlaceEntity entity, CancellationToken cancellationToken)
        {
            cancellationToken.ThrowIfCancellationRequested();
            ThrowIfDisposed();
            if (entity == null)
                throw new ArgumentNullException(nameof(entity));

            return await _organizationRepository.BookingPlace(entity, cancellationToken);
        }

        public async Task<IEnumerable<OrganizationBookingPlaceEntity>> GetOrganizationBookingPlacement(int organizationId, DateTime date, CancellationToken cancellationToken)
        {
            cancellationToken.ThrowIfCancellationRequested();
            ThrowIfDisposed();
            if (organizationId <= 0)
                throw new ArgumentException(nameof(organizationId));

            return await _organizationRepository.GetOrganizationBookingPlacement(organizationId, date, cancellationToken);
        }
    }
}
